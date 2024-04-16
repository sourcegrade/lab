package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.SubmissionGroupCategoryTable
import java.util.UUID

class SubmissionGroupCategory(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by SubmissionGroupCategoryTable.name
    val course by Course referencedOn SubmissionGroupCategoryTable.courseId
    val minSize by SubmissionGroupCategoryTable.minSize
    val maxSize by SubmissionGroupCategoryTable.maxSize

    companion object : EntityClass<UUID, SubmissionGroupCategory>(SubmissionGroupCategoryTable)
}
