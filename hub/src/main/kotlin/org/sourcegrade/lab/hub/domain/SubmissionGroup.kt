package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.SubmissionGroupTable
import java.util.UUID

class SubmissionGroup(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by SubmissionGroupTable.name
    val category by SubmissionGroupCategory referencedOn SubmissionGroupTable.categoryId

    companion object : EntityClass<UUID, SubmissionGroup>(SubmissionGroupTable)
}
