package org.sourcegrade.lab.hub.domain

import java.util.UUID

interface DomainEntity {
    val id: UUID
}

interface DomainFacet<E : DomainEntity> {
    suspend fun getOriginal(): E
}

interface Creates<E : DomainEntity>
