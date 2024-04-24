package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Assignment
import org.sourcegrade.lab.hub.domain.Course
import java.util.UUID

internal object Assignments : UUIDTable("sgl_assignments") {
    val createdUtc = timestamp("createdUtc")
    val courseId = reference("course_id", Courses)
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategories.id)

    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadline = timestamp("submissionDeadline")
}

internal class DBAssignment(id: EntityID<UUID>) : UUIDEntity(id), Assignment {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Assignments.createdUtc
    override val course: Course by DBCourse referencedOn Assignments.courseId
    override val submissionGroupCategory: DBSubmissionGroupCategory by DBSubmissionGroupCategory referencedOn Assignments.submissionGroupCategory

    override var name: String by Assignments.name
    override var description: String by Assignments.description
    override var submissionDeadlineUtc: Instant by Assignments.submissionDeadline
    override var submissionGroupCategoryId: UUID by SubmissionGroupCategories.referenceMutator(Assignments.submissionGroupCategory)

    companion object : EntityClass<UUID, DBAssignment>(Assignments)
}
