package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.domain.DomainEntity
import org.sourcegrade.lab.hub.domain.Repository
import java.util.UUID

internal class UUIDEntityClassRepository<E>(private val entityClass: EntityClass<UUID, E>) : Repository<E>
    where E : Entity<UUID>, E : DomainEntity {

    override suspend fun findById(id: UUID): E? =
        newSuspendedTransaction { entityClass.findById(id) }

    override suspend fun exists(id: UUID): Boolean =
        newSuspendedTransaction { entityClass.findById(id) != null }

    override suspend fun countAll(): Long =
        newSuspendedTransaction { entityClass.all().count() }

    override suspend fun deleteById(id: UUID): Boolean =
        newSuspendedTransaction {
            entityClass.findById(id)
                ?.let { it.delete(); true }
                ?: false
        }
}
