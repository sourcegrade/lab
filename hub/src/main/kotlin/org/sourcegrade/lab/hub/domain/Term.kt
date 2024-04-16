package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.TermTable
import java.util.UUID

//data class Term(
//    override val id: UUID,
//    val name: String,
//    val start: ZonedDateTime,
//    val end: ZonedDateTime,
//) : DomainEntity

class Term(id: EntityID<UUID>) : UUIDEntity(id) {
    val name by TermTable.name
    val start by TermTable.start
    val end by TermTable.end

    companion object : EntityClass<UUID, Term>(TermTable)
}
