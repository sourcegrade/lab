package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable

internal object Courses : UUIDTable("sgl_courses") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val term = varchar("term", 255)
    val ownerId = reference("owner_id", Users)
}
