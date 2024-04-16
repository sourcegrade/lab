package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.db.CourseTable.references
import org.sourcegrade.lab.hub.db.RolePermissionTable
import org.sourcegrade.lab.hub.db.RoleTable
import java.util.UUID

//data class Role(
//    override val id: UUID,
//    val scope: String,
//    val permissions: List<String>,
//) : DomainEntity

internal class Role(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by RoleTable.name
    val scope: String by RoleTable.scope
//    val permissions: SizedIterable<String> by ...? https://github.com/JetBrains/Exposed/issues/928
}
