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

package org.sourcegrade.lab.hub

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import graphql.schema.GraphQLType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.path
import io.ktor.server.routing.Routing
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.sourcegrade.lab.hub.db.assignment.Assignments
import org.sourcegrade.lab.hub.db.CourseMemberships
import org.sourcegrade.lab.hub.db.course.Courses
import org.sourcegrade.lab.hub.db.Criteria
import org.sourcegrade.lab.hub.db.DBModule
import org.sourcegrade.lab.hub.db.GradedCriteria
import org.sourcegrade.lab.hub.db.GradedRubrics
import org.sourcegrade.lab.hub.db.GradingRuns
import org.sourcegrade.lab.hub.db.Rubrics
import org.sourcegrade.lab.hub.db.SubmissionGroupCategories
import org.sourcegrade.lab.hub.db.SubmissionGroupMemberships
import org.sourcegrade.lab.hub.db.Submissions
import org.sourcegrade.lab.hub.db.Terms
import org.sourcegrade.lab.hub.db.user.Users
import org.sourcegrade.lab.hub.graphql.Scalars
import org.sourcegrade.lab.hub.graphql.UserMutations
import org.sourcegrade.lab.hub.graphql.UserQueries
import org.sourcegrade.lab.hub.http.authenticationModule
import kotlin.reflect.KType

fun Application.module() {
    install(Koin) {
        modules(
            module {
                single<Logger> { LogManager.getLogger("SGL Supervisor") }
                singleOf(::UserQueries)
                singleOf(::UserMutations)
            },
            DBModule,
        )
    }
    val databaseConfig =
        DatabaseConfig {
            keepLoadedReferencesOutOfTransaction = true
        }

    val logger by inject<Logger>()

    logger.info("Connecting to database...")
    Database.connect(
        url = getEnv("SGL_DB_URL"),
        driver = "org.postgresql.Driver",
        user = getEnv("SGL_DB_USER"),
        password = getEnv("SGL_DB_PASSWORD"),
        databaseConfig = databaseConfig,
    )
    logger.info("Finished connecting to database.")
    logger.info("Creating tables...")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            Assignments,
            CourseMemberships,
            Courses,
            Criteria,
            GradedCriteria,
            GradedRubrics,
            GradingRuns,
            Rubrics,
            SubmissionGroupCategories,
            SubmissionGroupMemberships,
            Submissions,
            Terms,
            Users,
        )
    }
    logger.info("Finished creating tables.")

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    install(GraphQL) {
        schema {
            hooks = object : SchemaGeneratorHooks {
                override fun willGenerateGraphQLType(type: KType): GraphQLType? = Scalars.willGenerateGraphQLType(type)
            }
            packages = listOf("org.sourcegrade.lab.hub")
            queries =
                listOf(
                    inject<UserQueries>().value,
                )
            mutations =
                listOf(
                    inject<UserMutations>().value,
                )
        }
    }

    install(Routing) {
        graphQLGetRoute()
        graphQLPostRoute()
        graphQLSDLRoute()
        graphiQLRoute()
    }

    authenticationModule()
    configureRouting()
    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}

internal fun getEnv(key: String): String = System.getenv(key) ?: throw IllegalStateException("$key not set in environment")
