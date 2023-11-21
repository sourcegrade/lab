package org.sourcegrade.yougrade.operator

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing

fun Application.module() {
    install(GraphQL) {
        schema {
            packages = listOf("com.example")
            queries = listOf(
//                HelloWorldQuery()
            )
        }
    }
    install(Routing) {
        graphQLPostRoute()
    }
}
