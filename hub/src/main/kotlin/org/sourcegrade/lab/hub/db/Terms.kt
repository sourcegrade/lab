package org.sourcegrade.lab.hub.db

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

internal object Terms : UUIDTable("sgl_terms") {
    val name = varchar("name", 255).uniqueIndex()
    val start = timestamp("start")
    val end = timestamp("end")
}

internal class Term(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by Terms.name
    val start: Instant by Terms.start
    val end: Instant by Terms.end

    companion object : EntityClass<UUID, Term>(Terms)
}
