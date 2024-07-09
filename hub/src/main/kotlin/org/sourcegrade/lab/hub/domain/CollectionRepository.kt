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

interface CollectionRepository<E : DomainEntity, C : DomainEntityCollection<E, C>> : Repository<E> {
    suspend fun findAll(
        limit: DomainEntityCollection.Limit? = null,
        orders: List<DomainEntityCollection.FieldOrdering> = emptyList(),
    ): C
}
//
//data class CollectionParameters(
//    val page: OptionalInput<Page>,
//    val order: List<FieldOrdering>,
//) {
//    data class Page(val num: Long, val size: OptionalInput<Int>)
//
//    data class FieldOrdering(val field: String, val sortOrder: SortOrder = SortOrder.DESC)
//
//    enum class SortOrder(val exposed: org.jetbrains.exposed.sql.SortOrder) {
//        ASC(org.jetbrains.exposed.sql.SortOrder.ASC),
//        DESC(org.jetbrains.exposed.sql.SortOrder.DESC),
//        ASC_NULLS_FIRST(org.jetbrains.exposed.sql.SortOrder.ASC_NULLS_FIRST),
//        DESC_NULLS_FIRST(org.jetbrains.exposed.sql.SortOrder.DESC_NULLS_FIRST),
//        ASC_NULLS_LAST(org.jetbrains.exposed.sql.SortOrder.ASC_NULLS_LAST),
//        DESC_NULLS_LAST(org.jetbrains.exposed.sql.SortOrder.DESC_NULLS_LAST)
//    }
//}
