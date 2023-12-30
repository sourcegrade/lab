package org.sourcegrade.yougrade.hub

import com.expediagroup.graphql.server.ktor.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing

fun Application.module() {
    install(GraphQL) {
        schema {
            packages = listOf("org.sourcegrade.yougrade.hub")
            queries = listOf(
                HelloWorldQuery(),
            )
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
