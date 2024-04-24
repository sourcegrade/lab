package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.SubmissionGroupCategories
import java.util.UUID

class SubmissionGroupCategory(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by SubmissionGroupCategories.name
    val course: Course by Course referencedOn SubmissionGroupCategories.courseId
    val minSize: Int by SubmissionGroupCategories.minSize
    val maxSize: Int by SubmissionGroupCategories.maxSize

    companion object : EntityClass<UUID, SubmissionGroupCategory>(SubmissionGroupCategories)
}
