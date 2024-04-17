package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.RolePermissionBindings
import org.sourcegrade.lab.hub.db.Roles
import java.util.UUID

class Role(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by Roles.name
    val scope: String by Roles.scope
    val permissionBindings: SizedIterable<RolePermissionBinding> by RolePermissionBinding referrersOn RolePermissionBindings.roleId

    companion object : EntityClass<UUID, Role>(Roles)
}

suspend fun Role.hasPermission(permission: String): Boolean? = newSuspendedTransaction {
    // TODO: hierarchical permissions
    RolePermissionBindings.select(RolePermissionBindings.value)
        .where { RolePermissionBindings.roleId eq this@hasPermission.id }
        .where { RolePermissionBindings.permission eq permission }
        .firstOrNull()
        ?.let { it[RolePermissionBindings.value] }
}
