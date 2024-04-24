package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.Submission
import org.sourcegrade.lab.hub.domain.SubmissionGroup
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

    override val courseMemberships: UserMembership.Accessor<Course> = object : UserMembership.Accessor<Course> {
        override suspend fun all(): SizedIterable<UserMembership<Course>> = newSuspendedTransaction {
            DBCourseMembership.find { CourseMemberships.userId eq id }
        }

        override suspend fun current(): SizedIterable<UserMembership<Course>> = newSuspendedTransaction {
            DBCourseMembership.find { CourseMemberships.userId eq id and CourseMemberships.endUtc.isNull() }
        }

        override suspend fun active(): SizedIterable<UserMembership<Course>> = newSuspendedTransaction {
            CourseMemberships.innerJoin(Courses).innerJoin(Terms).selectAll()
                .where { (CourseMemberships.userId eq id) and CourseMemberships.endUtc.isNull() }
                .where { (Terms.start lessEq Clock.System.now()) and (Terms.end greaterEq Clock.System.now()) }
                .mapLazy { DBCourseMembership.wrapRow(it) }
        }
    }

    override val submissionGroupsMemberships: UserMembership.Accessor<SubmissionGroup>
        get() = TODO("Not yet implemented")
    override val submissions: SizedIterable<Submission>
        get() = TODO("Not yet implemented")

    companion object : EntityClass<UUID, DBUser>(Users)
}
