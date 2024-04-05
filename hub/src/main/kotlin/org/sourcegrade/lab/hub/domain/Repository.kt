package org.sourcegrade.lab.hub.domain

import java.util.UUID

interface Repository<E : DomainEntity> {
    suspend fun getById(id: UUID): E?
    suspend fun exists(id: UUID): Boolean
    suspend fun countAll(): Long
    suspend fun deleteById(id: UUID): Boolean
}
