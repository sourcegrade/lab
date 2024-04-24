package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Course
import org.sourcegrade.lab.hub.domain.Term
import java.util.UUID

internal object Courses : UUIDTable("sgl_courses") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = varchar("term", 255)
    val ownerId = reference("owner_id", Users)
}

internal class DBCourse(id: EntityID<UUID>) : UUIDEntity(id), Course {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Courses.createdUtc
    override val term: Term by Term referencedOn Courses.term
    override val submissionGroupCategories: SizedIterable<DBSubmissionGroupCategory> by DBSubmissionGroupCategory referrersOn SubmissionGroupCategories.courseId
    override val assignments: SizedIterable<Assignment> by DBAssignment referrersOn Assignments.courseId
    override val owner: DBUser by DBUser referencedOn Courses.ownerId // TODO: Multiple owners

    override var name: String by Courses.name
    override var description: String by Courses.description
    override var ownerId: UUID by Users.referenceMutator(Courses.ownerId)

    companion object : EntityClass<UUID, DBCourse>(Courses)
}
