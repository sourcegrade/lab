package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

internal object RoleTable : UUIDTable("sgl_roles") {
    val scope = varchar("scope", 255)
}

internal class RoleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    val scope: String by RoleTable.scope
}
