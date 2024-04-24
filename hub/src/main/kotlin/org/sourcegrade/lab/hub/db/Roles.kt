package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Roles : UUIDTable("sgl_roles") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255)
    val scope = varchar("scope", 255)
}
