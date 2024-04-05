package org.sourcegrade.lab.hub.domain

import org.sourcegrade.lab.hub.db.DomainEntity
import java.time.ZonedDateTime
import java.util.UUID

data class Term(
    override val id: UUID,
    val name: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
) : DomainEntity
