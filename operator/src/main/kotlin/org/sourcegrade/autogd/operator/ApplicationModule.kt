package org.sourcegrade.autogd.operator

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.Logger
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

internal fun Application.module() {
    install(Koin) {
        modules(koinModule)
    }
//    install(GraphQL) {
//        schema {
//            packages = listOf("com.example")
//            queries = listOf(
//                HelloWorldQuery()
//            )
//        }
//    }
    install(Routing) {
//        graphQLPostRoute()
    }

    configureRouting()

    environment.monitor.subscribe(ApplicationStarted) {
        println("Hello")
        val logger by inject<Logger>()
        logger.info("Starting operator...")
        val operator by inject<Operator>()
        runBlocking {
            operator.initialize()
        }
    }
}
