package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
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
import org.sourcegrade.lab.hub.domain.Submission
import org.sourcegrade.lab.hub.domain.SubmissionGroup
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.User
import org.sourcegrade.lab.hub.domain.UserMembership
import java.util.UUID

internal object Users : UUIDTable("sgl_users") {
    val createdUtc = timestamp("createdUtc")
    val username = varchar("username", 255).uniqueIndex()
    val displayname = varchar("displayname", 255)
    val email = varchar("email", 255).uniqueIndex()
}

internal class DBUser(id: EntityID<UUID>) : UUIDEntity(id), User {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Users.createdUtc
    override var username: String by Users.username
    override var displayname: String by Users.displayname
    override var email: String by Users.email

    private fun Query.termPredicate(term: Term.Matcher, now: Instant): Query = when (term) {
        is Term.Matcher.All -> this
        is Term.Matcher.Current -> where { (Terms.start lessEq now) and (Terms.end greaterEq now) }
        is Term.Matcher.ByName -> where { Terms.name.eq(term.name) }
        is Term.Matcher.ById -> where { Terms.id.eq(term.id) }
    }

    private fun Query.membershipStatusPredicate(status: UserMembership.Status, now: Instant): Query = when (status) {
        UserMembership.Status.ALL -> this
        UserMembership.Status.FUTURE -> where { CourseMemberships.startUtc greater now }
        UserMembership.Status.PAST -> where { CourseMemberships.endUtc less now }
        UserMembership.Status.CURRENT -> where { CourseMemberships.endUtc.isNull() }
    }

    override suspend fun courseMemberships(
        status: UserMembership.Status,
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
        status: UserMembership.Status,
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
            .membershipStatusPredicate(UserMembership.Status.CURRENT, now)
            .mapLazy { DBAssignment.wrapRow(it) }
    }

    override suspend fun submissions(
        status: Submission.Status, // TODO: Use parameter
        term: Term.Matcher,
        now: Instant,
    ): SizedIterable<Submission> = newSuspendedTransaction {
        Submissions.innerJoin(SubmissionGroupMemberships) { SubmissionGroupMemberships.userId eq uuid }.selectAll()
            .termPredicate(term, now)
            .mapLazy { DBSubmission.wrapRow(it) }
    }

    companion object : EntityClass<UUID, DBUser>(Users)
}
