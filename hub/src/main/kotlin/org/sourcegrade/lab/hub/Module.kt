package org.sourcegrade.lab.hub

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.path
import io.ktor.server.routing.Routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.sourcegrade.lab.hub.http.authenticationModule
import org.sourcegrade.lab.hub.query.CourseMutations
import org.sourcegrade.lab.hub.query.CourseQueries
import org.sourcegrade.lab.hub.query.HelloWorldQuery
import org.sourcegrade.lab.hub.query.UserMutations
import org.sourcegrade.lab.hub.query.UserQueries

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
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    install(GraphQL) {
        schema {
            packages = listOf("org.sourcegrade.lab.hub")
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
