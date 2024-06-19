/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
