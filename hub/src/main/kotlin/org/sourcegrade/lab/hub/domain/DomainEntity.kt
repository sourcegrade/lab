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

package org.sourcegrade.lab.hub.domain

import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

interface DomainEntity {
    val uuid: UUID
    val createdUtc: Instant
}

interface Creates<out E : DomainEntity>

interface IdempotentCreates<out E : DomainEntity> : Creates<E> {
    val uuid: UUID
}

typealias Relation<T> = KProperty1<T, Any?>
