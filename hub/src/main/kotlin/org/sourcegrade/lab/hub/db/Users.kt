/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcegrade.lab.hub.db

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.Submission
import org.sourcegrade.lab.hub.domain.SubmissionGroup
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserMembership
import org.sourcegrade.lab.hub.domain.repo.MutableUserRepository
import java.util.UUID

internal object Users : UUIDTable("sgl_users") {
    val createdUtc = timestamp("createdUtc").clientDefault { Clock.System.now() }
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val displayname = varchar("displayname", 255)
}

@GraphQLIgnore
internal class DBUser(id: EntityID<UUID>) : UUIDEntity(id), User {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Users.createdUtc
    override var email: String by Users.email
    override var username: String by Users.username
    override var displayname: String by Users.displayname

    private fun Query.termPredicate(term: Term.Matcher, now: Instant): Query = when (term) {
        is Term.Matcher.All -> this
        is Term.Matcher.Current -> where { (Terms.start lessEq now) and (Terms.end greaterEq now) }
        is Term.Matcher.ByName -> where { Terms.name.eq(term.name) }
        is Term.Matcher.ById -> where { Terms.id.eq(term.id) }
    }

    private fun Query.membershipStatusPredicate(status: UserMembership.UserMembershipStatus, now: Instant): Query = when (status) {
        UserMembership.UserMembershipStatus.ALL -> this
        UserMembership.UserMembershipStatus.FUTURE -> where { CourseMemberships.startUtc greater now }
        UserMembership.UserMembershipStatus.PAST -> where { CourseMemberships.endUtc less now }
        UserMembership.UserMembershipStatus.CURRENT -> where { CourseMemberships.endUtc.isNull() }
    }

    override suspend fun courseMemberships(
        status: UserMembership.UserMembershipStatus,
        term: Term.Matcher,
        now: Instant,
    ): SizedIterable<UserMembership<Course>> = newSuspendedTransaction {
        CourseMemberships.innerJoin(Courses).innerJoin(Terms).selectAll()
            .where { (CourseMemberships.userId eq uuid) }
            .termPredicate(term, now)
            .membershipStatusPredicate(status, now)
            .mapLazy { DBCourseMembership.wrapRow(it) }
    }

    override suspend fun submissionGroupMemberships(
        status: UserMembership.UserMembershipStatus,
        term: Term.Matcher,
        now: Instant,
    ): SizedIterable<UserMembership<SubmissionGroup>> = newSuspendedTransaction {
        SubmissionGroupMemberships.innerJoin(SubmissionGroups).innerJoin(Terms).selectAll()
            .where { (CourseMemberships.userId eq uuid) }
            .termPredicate(term, now)
            .membershipStatusPredicate(status, now)
            .mapLazy { DBSubmissionGroupMembership.wrapRow(it) }
    }

    override suspend fun assignments(
        term: Term.Matcher,
        now: Instant,
    ): SizedIterable<Assignment> = newSuspendedTransaction {
        Assignments.innerJoin(Courses).innerJoin(Terms).innerJoin(CourseMemberships).selectAll()
            .where { CourseMemberships.userId eq uuid }
            .termPredicate(term, now)
            .membershipStatusPredicate(UserMembership.UserMembershipStatus.CURRENT, now)
            .mapLazy { DBAssignment.wrapRow(it) }
    }

    override suspend fun submissions(
        status: Submission.SubmissionStatus, // TODO: Use parameter
        term: Term.Matcher,
        now: Instant,
    ): SizedIterable<Submission> = newSuspendedTransaction {
        Submissions.innerJoin(SubmissionGroupMemberships) { SubmissionGroupMemberships.userId eq uuid }.selectAll()
            .termPredicate(term, now)
            .mapLazy { DBSubmission.wrapRow(it) }
    }

    companion object : EntityClass<UUID, DBUser>(Users)
}

internal class DBUserRepository(
    private val logger: Logger,
) : MutableUserRepository, Repository<User> by UUIDEntityClassRepository(DBUser) {
    override suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
        DBUser.find { Users.username eq username }.firstOrNull()
    }

    override suspend fun findAllByUsername(partialUsername: String): SizedIterable<User.Snapshot> = newSuspendedTransaction {
        DBUser.find { Users.username like "%$partialUsername%" }.mapLazy { it.toSnapshot() }
    }

    override suspend fun findByEmail(email: String): User? = newSuspendedTransaction {
        DBUser.find { Users.email eq email }.firstOrNull()
    }

    override suspend fun create(item: User.CreateDto): User = newSuspendedTransaction {
        DBUser.new {
            username = item.username
            displayname = item.displayname
            email = item.email
        }.also {
            logger.info("Created new user ${it.uuid} with data $item")
        }
    }

    override suspend fun put(item: User.CreateDto): MutableRepository.PutResult<User> = newSuspendedTransaction {
        val existingUser = findByUsername(item.username)
        if (existingUser == null) {
            MutableRepository.PutResult(create(item), created = true)
        } else {
            logger.info(
                "Loaded existing user ${existingUser.username} (${existingUser.uuid}) with" +
                    "display name ${existingUser.displayname} and email ${existingUser.email}",
            )
            MutableRepository.PutResult(existingUser, created = false)
        }
    }
}
