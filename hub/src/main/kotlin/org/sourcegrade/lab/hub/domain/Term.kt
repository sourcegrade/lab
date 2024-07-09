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

import graphql.schema.DataFetchingEnvironment
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.sourcegrade.lab.hub.db.Terms
import org.sourcegrade.lab.hub.graphql.extractRelations
import java.util.UUID

interface Term : DomainEntity {
    val name: String
    val start: Instant
    val end: Instant?

    data class CreateDto(
        val name: String,
        val start: Instant,
        val end: Instant?,
    ) : Creates<Term>

    sealed interface Matcher {
        data object All : Matcher
        data object Current : Matcher
        data class ByName(val name: String) : Matcher {
            init {
                require(name.isNotBlank())
            }

            override fun toString(): String = "ByName($name)"
        }

        data class ById(val id: UUID) : Matcher {
            override fun toString(): String = "ById($id)"
        }

        companion object {
            fun fromString(value: String): Matcher = when (value) {
                "All" -> All
                "Current" -> Current
                else -> {
                    regex.matchEntire(value)?.let { match ->
                        val type = match.groups["type"]?.value
                        val param = match.groups["param"]?.value!!
                        when (type) {
                            "ByName" -> ByName(param)
                            "ById" -> ById(UUID.fromString(param))
                            else -> throw IllegalArgumentException("Invalid type: $type")
                        }
                    } ?: throw IllegalArgumentException("Invalid matcher: $value")
                }
            }

            fun fromNullableString(value: String?): Matcher = value?.let { fromString(it) } ?: Current

            private val regex = "(?<type>[a-zA-Z])\\((?<param>.+\\))".toRegex()
        }
    }
}

interface TermRepository : CollectionRepository<Term, TermCollection> {
    suspend fun findByName(name: String, relations: List<Relation<Term>>): Term?
    suspend fun findByTime(now: Instant, relations: List<Relation<Term>>): Term?
}

interface MutableTermRepository : TermRepository, MutableRepository<Term, Term.CreateDto>

interface TermCollection : DomainEntityCollection<Term, TermCollection> {
    override suspend fun count(): Long
    override suspend fun empty(): Boolean

    suspend fun list(dfe: DataFetchingEnvironment): List<Term> = list(dfe.extractRelations())
}

internal fun SqlExpressionBuilder.termPredicate(term: Term.Matcher, now: Instant): Op<Boolean> = when (term) {
    is Term.Matcher.All -> Op.TRUE
    is Term.Matcher.Current -> (Terms.start lessEq now) and (Terms.end greaterEq now)
    is Term.Matcher.ByName -> Terms.name.eq(term.name)
    is Term.Matcher.ById -> Terms.id.eq(term.id)
}
