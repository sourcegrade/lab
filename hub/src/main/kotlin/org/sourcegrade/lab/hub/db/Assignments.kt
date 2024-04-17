package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Assignments : UUIDTable("sgl_assignments") {
    val courseId = reference("course_id", Courses)
    val name = varchar("name", 255).uniqueIndex()
    val description = varchar("description", 16 * 1024)
    val submissionDeadLine = timestamp("submissionDeadline") // TODO: time zone info
    val submissionGroupCategory = reference("submission_group_category", SubmissionGroupCategories.id)
}
