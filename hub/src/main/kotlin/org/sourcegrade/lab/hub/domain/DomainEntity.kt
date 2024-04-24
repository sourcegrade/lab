package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface DomainEntity {
    val uuid: UUID
    val createdUtc: Instant
}
