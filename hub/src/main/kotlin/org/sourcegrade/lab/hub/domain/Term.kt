package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.Terms
import java.util.UUID

//data class Term(
//    override val id: UUID,
//    val name: String,
//    val start: ZonedDateTime,
//    val end: ZonedDateTime,
//) : DomainEntity

class Term(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by Terms.name
    val start by Terms.start
    val end by Terms.end

    companion object : EntityClass<UUID, Term>(Terms)
}
