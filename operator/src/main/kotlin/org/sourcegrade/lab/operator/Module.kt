package org.sourcegrade.lab.operator

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.operations.Query
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.Logger
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

class HelloWorldQuery : Query {
    fun hello(): String = "Hello World!"
}

fun Application.module() {

    install(Koin) {
        modules(koinModule)
    }

    install(GraphQL) {
        schema {
            packages = listOf("org.sourcegrade.lab.operator")
            queries = listOf(
                HelloWorldQuery(),
            )
        }
    }
    install(Routing) {
        graphQLPostRoute()
    }

    configureRouting()

    environment.monitor.subscribe(ApplicationStarted) {
        val logger by inject<Logger>()
        logger.info("Starting operator...")
        val operator by inject<Operator>()
        runBlocking {
            operator.initialize()
        }
    }
}
