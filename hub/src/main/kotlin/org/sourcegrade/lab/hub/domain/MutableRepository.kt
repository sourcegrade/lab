package org.sourcegrade.lab.hub.domain

interface MutableRepository<E : DomainEntity, C : Creates<E>> : Repository<E> {
    suspend fun create(item: C): E
    suspend fun put(item: C): PutResult<E>

    data class PutResult<E : DomainEntity>(
        val entity: E,
        val created: Boolean,
    )
}
