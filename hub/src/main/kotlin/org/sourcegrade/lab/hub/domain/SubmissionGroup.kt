package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.SubmissionGroupMembers
import org.sourcegrade.lab.hub.db.SubmissionGroups
import java.util.UUID

class SubmissionGroup(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by SubmissionGroups.name
    val category by SubmissionGroupCategory referencedOn SubmissionGroups.categoryId
    val members by User via SubmissionGroupMembers

    companion object : EntityClass<UUID, SubmissionGroup>(SubmissionGroups)
}
