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

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.mapLazy
import org.sourcegrade.lab.hub.domain.DomainEntity
import org.sourcegrade.lab.hub.domain.ExecutionContext
import org.sourcegrade.lab.hub.domain.Relation

interface EntityConversionContext<E : DomainEntity, N : UUIDEntity> {

    fun createSnapshot(entity: N, relations: Set<String>): E

    fun convertRelation(relation: Relation<E>): Relation<N>

    suspend fun <T> entityConversion(
        executionContext: ExecutionContext,
        relations: List<Relation<E>> = emptyList(),
        statement: ConversionBody<E, N, T>,
    ): T
}

typealias ConversionBody<E, N, T> = suspend EntityConversion<E, N>.() -> EntityConversion.BindResult<T>

class EntityConversionContextImpl<E : DomainEntity, N : UUIDEntity>(
    private val snapshotFun: (N, Set<String>) -> E,
) : EntityConversionContext<E, N> {

    override fun createSnapshot(entity: N, relations: Set<String>): E = snapshotFun(entity, relations)

    override fun convertRelation(relation: Relation<E>): Relation<N> {
        @Suppress("UNCHECKED_CAST")
        return (relation as Relation<N>)
    }

    override suspend fun <T> entityConversion(
        executionContext: ExecutionContext,
        relations: List<Relation<E>>,
        statement: ConversionBody<E, N, T>,
    ): T = executionContext.execute {
        val ec = EntityConversion(context = this, relations)
        statement(ec).result
    }
}

@DslMarker
annotation class EntityConversionDsl

class EntityConversion<E : DomainEntity, N : UUIDEntity>(
    private val context: EntityConversionContext<E, N>,
    relations: List<Relation<E>>,
) {

    private val relationSet: Set<String> = relations.map { it.name }.toSet()
    private val relationArray: Array<Relation<N>> = relations.map { context.convertRelation(it) }.toTypedArray()
    private fun N.createSnapshot(): E = context.createSnapshot(this, relationSet)

    @EntityConversionDsl
    fun N?.bindNullable(): BindResult<E?> =
        this?.bind() ?: BindResult(null)

    @EntityConversionDsl
    fun N.bind(): BindResult<E> =
        BindResult((if (relationArray.isNotEmpty()) load(*relationArray) else this).createSnapshot())

    @EntityConversionDsl
    fun SizedIterable<N>.bindIterable(): BindResult<SizedIterable<E>> =
        BindResult((if (relationArray.isNotEmpty()) with(*relationArray) else this).mapLazy { it.createSnapshot() })

    @EntityConversionDsl
    fun <T> T.bindT(): BindResult<T> = BindResult(this)

    class BindResult<out T>(val result: T)
}
