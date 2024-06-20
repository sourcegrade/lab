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

package org.sourcegrade.lab.hub.graphql

import graphql.schema.DataFetchingEnvironment
import org.sourcegrade.lab.hub.domain.DomainEntity
import org.sourcegrade.lab.hub.domain.Relation
import kotlin.reflect.KProperty1

internal inline fun <reified E : DomainEntity> DataFetchingEnvironment.extractRelations(): List<Relation<E>> =
    selectionSet.immediateFields.map { field -> coerceRelation<E>(field.name) }

internal inline fun <reified E : DomainEntity> coerceRelation(relation: String): Relation<E> =
    E::class.members.find { it.name == relation }?.let {
        @Suppress("UNCHECKED_CAST")
        it as KProperty1<E, Any?>
    } ?: error("No relation $relation found on ${E::class.simpleName}, available relations are: ${E::class.members.map { it.name }}")
