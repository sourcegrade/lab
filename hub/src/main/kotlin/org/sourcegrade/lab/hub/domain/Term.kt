package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.Terms
import java.util.UUID

class Term(id: EntityID<UUID>) : UUIDEntity(id) {
    val name: String by Terms.name
    val start: Instant by Terms.start
    val end: Instant by Terms.end

    companion object : EntityClass<UUID, Term>(Terms)
}
