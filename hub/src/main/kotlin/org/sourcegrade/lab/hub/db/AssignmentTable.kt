package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.db.AssignmentEntity.Companion.referrersOn
import org.sourcegrade.lab.hub.db.RolePermissionTable.references
import org.sourcegrade.lab.hub.domain.Course
import java.util.UUID

internal object AssignmentTable : UUIDTable("sgl_assignments") {
    val courseId = reference("course_id", CourseTable.id)
    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadLine = timestamp("submissionDeadline") // TODO: time zone info
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategoryTable.id)
}

internal class AssignmentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    val courseId by AssignmentTable.courseId references CourseTable.id
    val name by AssignmentTable.name
    val description by AssignmentTable.description

    companion object : EntityClass<UUID, AssignmentEntity>(AssignmentTable)
}
