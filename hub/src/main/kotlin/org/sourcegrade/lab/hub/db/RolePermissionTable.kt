package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.sql.Table

internal object RolePermissionTable : Table("sgl_role_permissions") {
    val roleId = reference("role_id", RoleTable.id)
    val permission = varchar("permission", 1024)
}
