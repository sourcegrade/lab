package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object Roles : UUIDTable("sgl_roles") {
    val name = varchar("name", 255)
    val scope = varchar("scope", 255)
}
