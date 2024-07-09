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

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sourcegrade.lab.hub.domain.Term
import java.util.UUID

internal object Terms : UUIDTable("sgl_terms") {
    val name = varchar("name", 255).uniqueIndex()
    val createdUtc = timestamp("createdUtc")
    val start = timestamp("start")
    val end = timestamp("end")
}

@GraphQLIgnore
internal class DBTerm(id: EntityID<UUID>) : UUIDEntity(id), Term {
    override val uuid: UUID = id.value
    override val createdUtc: Instant by Terms.createdUtc
    override val name: String by Terms.name
    override val start: Instant by Terms.start
    override val end: Instant by Terms.end

    companion object : EntityClass<UUID, DBTerm>(Terms)
}

internal fun DBTerm.Companion.getCurrentOrNull(): DBTerm? =
    DBTerm.all().orderBy(Terms.start to SortOrder.DESC).firstOrNull()

internal fun DBTerm.Companion.getCurrent(): DBTerm =
    checkNotNull(getCurrentOrNull()) { "No current term found" }
