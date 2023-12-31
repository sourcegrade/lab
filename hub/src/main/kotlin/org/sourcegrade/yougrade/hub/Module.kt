package org.sourcegrade.yougrade.hub

import com.expediagroup.graphql.server.ktor.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.*
import io.ktor.server.routing.Routing
import org.jetbrains.exposed.sql.Database
import org.sourcegrade.yougrade.hub.models.Users
import org.sourcegrade.yougrade.hub.queries.HelloWorldQuery
import org.sourcegrade.yougrade.hub.queries.UsersEndpoints

fun Application.module() {
    val database =
        Database.connect(
            "jdbc:postgresql://localhost:5432/yougrade",
            driver = "org.postgresql.Driver",
            user = "admin",
            password = "admin",
        )
    install(GraphQL) {
        schema {
            packages = listOf("org.sourcegrade.yougrade.hub")
            queries =
                listOf(
                    HelloWorldQuery(),
                    UsersEndpoints(),
                )
        }
    }
    install(Authentication) {
        // local authentication
        basic {
            realm = "ktor"
            validate { credentials ->
                if (Users.validateUser(username = credentials.name, password = credentials.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
    install(Routing) {
        graphQLGetRoute()
        graphQLPostRoute()
//        graphQLSubscriptionsRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}
