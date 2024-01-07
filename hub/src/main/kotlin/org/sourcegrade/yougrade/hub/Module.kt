package org.sourcegrade.yougrade.hub

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.http.Url
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.path
import io.ktor.server.routing.Routing
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
            environment.config.tryGetString("ktor.deployment.url") ?: throw IllegalStateException("No deployment url set"),
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

//    install(ContentNegotiation) {
//        json(
//            Json {
//                prettyPrint = true
//                isLenient = true
//                ignoreUnknownKeys = true
//            },
//        )
//    }

    authenticationModule()
    configureRouting()
    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}
