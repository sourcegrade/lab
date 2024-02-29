package org.sourcegrade.yougrade.hub

import com.expediagroup.graphql.server.ktor.*
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.path
import io.ktor.server.routing.Routing
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.sourcegrade.yougrade.hub.http.authenticationModule
import org.sourcegrade.yougrade.hub.queries.CourseMutations
import org.sourcegrade.yougrade.hub.queries.CourseQueries
import org.sourcegrade.yougrade.hub.queries.HelloWorldQuery
import org.sourcegrade.yougrade.hub.queries.UserMutations
import org.sourcegrade.yougrade.hub.queries.UserQueries
import kotlin.collections.listOf

fun Application.module() {
    val environment = environment
    val url =
        Url(
            environment.config.tryGetString("ktor.deployment.url")
                ?: throw IllegalStateException("No deployment url set"),
        )

    val databaseConfig =
        DatabaseConfig {
            keepLoadedReferencesOutOfTransaction = true
        }

    Database.connect(
        environment.config.tryGetString("ktor.db.url") ?: "",
        driver = "org.postgresql.Driver",
        user = environment.config.tryGetString("ktor.db.user") ?: "",
        password = environment.config.tryGetString("ktor.db.password") ?: "",
        databaseConfig = databaseConfig,
    )

    install(CORS) {
//        allowHost("localhost:3000", schemes = listOf("http", "https"))
        anyHost()
    }

    install(GraphQL) {
        schema {
            packages = listOf("org.sourcegrade.yougrade.hub")
            queries =
                listOf(
                    HelloWorldQuery(),
                    UserQueries(),
                    CourseQueries(),
                )
            mutations =
                listOf(
                    UserMutations(),
                    CourseMutations(),
                )
        }
    }

    install(Routing) {
        graphQLGetRoute()
        graphQLPostRoute()
        graphQLSDLRoute()
//        graphQLSubscriptionsRoute()
        graphiQLRoute()
    }

    authenticationModule()
    configureRouting()
    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}
