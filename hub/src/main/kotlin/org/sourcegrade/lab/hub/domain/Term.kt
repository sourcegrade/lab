package org.sourcegrade.lab.hub.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Term(
    override val id: UUID,
    val name: String,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
) : DomainEntity
