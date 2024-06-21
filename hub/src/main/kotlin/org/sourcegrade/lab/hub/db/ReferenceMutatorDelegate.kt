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
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.companionObjectInstance

internal suspend inline fun <ID : Comparable<ID>, reified E : Entity<ID>> KMutableProperty0<E>.mutateReference(id: ID): Boolean {
    @Suppress("UNCHECKED_CAST")
    val entityClass: EntityClass<ID, E> =
        checkNotNull(E::class.companionObjectInstance as? EntityClass<ID, E>) {
            "Companion EntityClass for ${E::class} not found"
        }

    return newSuspendedTransaction {
        val entity = entityClass.findById(id)
        if (entity == null) {
            false
        } else {
            set(entity)
            true
        }
    }
}

internal inline fun <ID : Comparable<ID>, reified E : Entity<ID>> EntityClass<ID, E>.findByIdNotNull(id: ID): E =
    findById(id) ?: error("${E::class.simpleName} $id not found")
