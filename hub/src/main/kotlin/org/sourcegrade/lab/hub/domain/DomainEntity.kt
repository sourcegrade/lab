package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID

interface DomainEntity {
    val uuid: UUID
    val createdUtc: Instant
}

interface Creates<out E : DomainEntity>

interface IdempotentCreates<out E : DomainEntity> : Creates<E> {
    val uuid: UUID
}
