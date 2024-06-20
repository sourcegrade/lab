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

import org.sourcegrade.lab.hub.domain.DomainEntity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

internal sealed interface RelationOption<out T> : ReadOnlyProperty<DomainEntity, T> {
    data class Loaded<out T>(val value: T) : RelationOption<T> {
        override fun getValue(thisRef: DomainEntity, property: KProperty<*>): T = value
    }

    data object NotLoaded : RelationOption<Nothing> {
        override fun getValue(thisRef: DomainEntity, property: KProperty<*>): Nothing {
            throw RelationNotLoadedException(thisRef::class, property.name)
        }
    }

    companion object {
        fun <T> of(property: KProperty0<T>, predicate: (String) -> Boolean): RelationOption<T> =
            if (predicate(property.name)) Loaded(property.get()) else NotLoaded

        context(Set<String>)
        fun <T> of(property: KProperty0<T>): RelationOption<T> = of(property) { contains(property.name) }
    }
}

class RelationNotLoadedException internal constructor(entity: KClass<out DomainEntity>, relation: String) :
    RuntimeException("Relation $relation not loaded for entity ${entity.simpleName}")
