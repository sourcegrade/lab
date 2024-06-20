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

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Locale
import java.util.UUID
import kotlin.reflect.KType
import kotlin.time.Duration

internal object Scalars {

    fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier) {
            Instant::class -> instant
            UUID::class -> uuid
            Long::class -> long
            Duration::class -> duration
            SizedIterable::class -> sizedIterable
            else -> null
        }

    private val instant: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Instant")
        .coercing(
            object : Coercing<Instant, String> {
                override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): Instant =
                    Instant.parse(input as String)

                override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                    (dataFetcherResult as Instant).toString()
            },
        ).build()

    private val uuid = GraphQLScalarType.newScalar()
        .name("UUID")
        .coercing(
            object : Coercing<UUID, String> {
                override fun parseLiteral(
                    input: Value<*>,
                    variables: CoercedVariables,
                    graphQLContext: GraphQLContext,
                    locale: Locale,
                ): UUID = UUID.fromString((input as StringValue).value)

                override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): UUID =
                    UUID.fromString(input.toString())

                override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                    (dataFetcherResult as UUID).toString()
            },
        ).build()

    private val long = GraphQLScalarType.newScalar()
        .name("Long")
        .coercing(
            object : Coercing<Long, String> {
                override fun parseLiteral(
                    input: Value<*>,
                    variables: CoercedVariables,
                    graphQLContext: GraphQLContext,
                    locale: Locale,
                ): Long = (input as IntValue).value.toLong()

                override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): Long =
                    (input as Int).toLong()

                override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                    dataFetcherResult.toString()
            },
        ).build()

    private val duration = GraphQLScalarType.newScalar()
        .name("Duration")
        .coercing(
            object : Coercing<Duration, String> {
                override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): Duration =
                    Duration.parse(input as String)

                override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                    (dataFetcherResult as Duration).toString()
            },
        ).build()

    private val sizedIterable = GraphQLScalarType.newScalar()
        .name("SizedIterable")
        .coercing(
            object : Coercing<SizedIterable<*>, List<*>> {
                override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): List<*> {
                    val result = transaction { (dataFetcherResult as SizedIterable<*>).toList() }
                    println("SizedIterable serialized to $result")
                    return result
                }
            },
        ).build()
}
