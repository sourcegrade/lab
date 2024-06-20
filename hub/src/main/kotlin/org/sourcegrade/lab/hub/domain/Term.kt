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
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.sourcegrade.lab.hub.db.Terms
import java.util.UUID

interface Term : DomainEntity {
    val name: String
    val start: Instant
    val end: Instant?

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

            private val regex = "(?<type>[a-zA-Z])\\((?<param>.+\\))".toRegex()
        }
    }
}

internal fun Query.termPredicate(term: Term.Matcher, now: Instant): Query = when (term) {
    is Term.Matcher.All -> this
    is Term.Matcher.Current -> where { (Terms.start lessEq now) and (Terms.end greaterEq now) }
    is Term.Matcher.ByName -> where { Terms.name.eq(term.name) }
    is Term.Matcher.ById -> where { Terms.id.eq(term.id) }
}
