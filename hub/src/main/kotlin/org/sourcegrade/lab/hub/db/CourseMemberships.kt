package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.UserMembership
import java.util.UUID

internal object CourseMemberships : UUIDTable("sgl_course_membership") {
    val createdUtc = timestamp("createdUtc")
    val userId = reference("user_id", Users)
    val startUtc = timestamp("startUtc")
    val endUtc = timestamp("endUtc").nullable()
    val courseId = reference("course_id", Courses)
}

internal class DBCourseMembership(id: EntityID<UUID>) : UUIDEntity(id), UserMembership<Course> {
    override val uuid: UUID = id.value
    override val createdUtc by CourseMemberships.createdUtc
    override val startUtc by CourseMemberships.startUtc
    override val endUtc by CourseMemberships.endUtc
    override val user: DBUser by DBUser referencedOn CourseMemberships.userId
    override val target: DBCourse by DBCourse referencedOn CourseMemberships.courseId

    companion object : EntityClass<UUID, DBCourseMembership>(CourseMemberships)
}
