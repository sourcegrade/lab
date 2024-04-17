package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.RolePermissionBindings
import java.util.UUID

class RolePermissionBinding(id: EntityID<UUID>) : UUIDEntity(id) {
    val roleId by RolePermissionBindings.roleId
    val permission by RolePermissionBindings.permission
    val value by RolePermissionBindings.value

    companion object : EntityClass<UUID, RolePermissionBinding>(RolePermissionBindings)
}
