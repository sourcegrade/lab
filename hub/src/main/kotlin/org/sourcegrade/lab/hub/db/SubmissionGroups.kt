package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object SubmissionGroups : UUIDTable("sgl_submission_groups") {
    val name = varchar("name", 255).uniqueIndex()
    val categoryId = reference("category_id", SubmissionGroupCategories)
}
