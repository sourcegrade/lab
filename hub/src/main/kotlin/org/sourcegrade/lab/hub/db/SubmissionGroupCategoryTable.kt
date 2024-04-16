package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object SubmissionGroupCategoryTable : UUIDTable("sgl_submission_group_categories") {
    val name = varchar("name", 255).uniqueIndex()
    val courseId = reference("course_id", CourseTable)
    val minSize = integer("min_size")
    val maxSize = integer("max_size")
}
