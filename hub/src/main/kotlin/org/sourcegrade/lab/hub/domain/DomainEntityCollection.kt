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

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Table
import org.sourcegrade.lab.hub.db.ConversionBody
import org.sourcegrade.lab.hub.db.EntityConversionContext
import org.jetbrains.exposed.sql.SortOrder as ExposedSortOrder

@GraphQLIgnore
interface DomainEntityCollection<E : DomainEntity, C : DomainEntityCollection<E, C>> {

    fun limit(num: Int, offset: Long = 0): C

    fun page(page: Int, pageSize: Int): C = limit(pageSize, (page - 1) * pageSize.toLong())

    fun orderBy(orders: List<FieldOrdering>): C

    @GraphQLIgnore
    suspend fun list(relations: List<Relation<E>> = emptyList()): List<E>

    suspend fun count(): Long

    suspend fun empty(): Boolean

    data class FieldOrdering(val field: String, val sortOrder: SortOrder = SortOrder.DESC)

    enum class SortOrder(val exposed: ExposedSortOrder) {
        ASC(ExposedSortOrder.ASC),
        DESC(ExposedSortOrder.DESC),
        ASC_NULLS_FIRST(ExposedSortOrder.ASC_NULLS_FIRST),
        DESC_NULLS_FIRST(ExposedSortOrder.DESC_NULLS_FIRST),
        ASC_NULLS_LAST(ExposedSortOrder.ASC_NULLS_LAST),
        DESC_NULLS_LAST(ExposedSortOrder.DESC_NULLS_LAST)
    }
}

internal class SizedIterableCollection<E : DomainEntity, N : UUIDEntity, C : DomainEntityCollection<E, C>>(
    private val table: Table,
    private val conversionContext: EntityConversionContext<E, N>,
    private val ctor: (Pair<Int, Long>?, List<DomainEntityCollection.FieldOrdering>, ConversionBody<E, N, SizedIterable<E>>) -> C,
    private val limit: Pair<Int, Long>?,
    private val orders: List<DomainEntityCollection.FieldOrdering>,
    private val body: ConversionBody<E, N, SizedIterable<E>>,
) : DomainEntityCollection<E, C>, EntityConversionContext<E, N> by conversionContext {
    override fun limit(num: Int, offset: Long): C =
        ctor(limit?.let { (n, o) -> minOf(num, n) to offset + o } ?: (num to offset), orders, body)

    // Note that this is not *technically* correct because nested orderings are not supported
    // But its close enough, because it would only be noticeable in an order -> limit -> order scenario
    override fun orderBy(orders: List<DomainEntityCollection.FieldOrdering>): C =
        ctor(limit, orders, body)

    override suspend fun list(relations: List<Relation<E>>): List<E> =
        entityConversion(relations) {
            body().result
                .let { if (limit != null) it.limit(limit.first, limit.second) else it }
                .let { if (orders.isNotEmpty()) it.orderBy(table, orders) else it }
                .toList().bindT()
        }

    override suspend fun count(): Long = entityConversion { body().result.count().bindT() }

    override suspend fun empty(): Boolean = entityConversion { body().result.empty().bindT() }
}

private fun <E : DomainEntity> SizedIterable<E>.orderBy(
    table: Table,
    orders: List<DomainEntityCollection.FieldOrdering>,
): SizedIterable<E> = orderBy(
    *orders.map { order ->
        table::class.members.find { it.name == order.field }?.call(table) as Expression<*> to order.sortOrder.exposed
    }.toTypedArray(),
)
