package org.sourcegrade.lab.example.grader

import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.contentType
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.sourcegrade.lab.model.GraderLaunchOuterClass.GraderLaunch
import org.sourcegrade.lab.model.ProtobufContentConverter

fun main() {
    val client = HttpClient()
    embeddedServer(Netty, module = { module(client) })
        .start(wait = true)
}

fun Application.module(client: HttpClient) {

    install(Routing)
    install(Koin) {
        modules(
            module {
                single<Logger> { LogManager.getLogger("SGL Grader Example") }
            },
        )
    }

    install(ContentNegotiation) {
        register(ContentType.Application.ProtoBuf, ProtobufContentConverter())
    }


    routing {
        val logger by inject<Logger>()
        post("/launch") {
            val contentType = call.request.contentType()
            logger.warn("Content-Type: $contentType")
            val graderLaunch = call.receive<GraderLaunch>()
            logger.info("Received $graderLaunch")
            call.respondText("Success lang: ${graderLaunch.language}")
        }
    }
}
