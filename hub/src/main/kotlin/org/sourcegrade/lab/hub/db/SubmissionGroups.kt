package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object SubmissionGroups : UUIDTable("sgl_submission_groups") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255).uniqueIndex()
    val categoryId = reference("category_id", SubmissionGroupCategories)
}
