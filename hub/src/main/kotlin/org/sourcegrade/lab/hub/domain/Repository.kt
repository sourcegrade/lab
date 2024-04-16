package org.sourcegrade.lab.hub.domain

import java.util.UUID
import kotlin.reflect.KClass

interface Repository<E : DomainEntity> {
    val entityType: KClass<E>
    suspend fun getById(id: UUID): E?
    suspend fun exists(id: UUID): Boolean
    suspend fun countAll(): Long
    suspend fun deleteById(id: UUID): Boolean
}
