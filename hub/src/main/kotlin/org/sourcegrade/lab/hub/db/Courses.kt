package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object Courses : UUIDTable("sgl_courses") {
    val createdUtc = timestamp("createdUtc")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = varchar("term", 255)
    val ownerId = reference("owner_id", Users)
}
