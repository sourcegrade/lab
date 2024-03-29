package org.sourcegrade.lab.example.grader

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.origin
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.sourcegrade.lab.model.GraderLaunchOuterClass.GraderLaunch
import org.sourcegrade.lab.model.ProtobufContentConverter
import java.util.concurrent.Executors

val pool = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

fun main() {
    val client = HttpClient()

    embeddedServer(Netty, port = 8080) { module(client) }
        .start(wait = true)
}

fun Application.module(client: HttpClient) {
    install(Routing)
    install(Koin) {
        modules(module { single<Logger> { LogManager.getLogger("SGL Grader Example") } })
    }
    install(ContentNegotiation) {
        register(ContentType.Application.ProtoBuf, ProtobufContentConverter)
    }

    routing {
        val logger by inject<Logger>()
        post("/launch") {
            val graderLaunch = call.receive<GraderLaunch>()
            logger.info("Received grader launch request from ${call.request.origin.remoteAddress}")

            if (graderLaunch.language != "java") {
                logger.error("Grader launch request has incorrect language '${graderLaunch.language}'. Expected 'java'")
                return@post call.respondText("This grader only supports 'java'", status = HttpStatusCode.BadRequest)
            }

            async(pool) {
                withContext(pool) {
                    // simulate grading
                    delay(5000)

                    client.post(graderLaunch.rubricWebhook) {
                        contentType(ContentType.Text.Plain)
                        setBody("Example rubric") // TODO: Return real rubric
                    }
                }
            }.invokeOnCompletion {
                if (it != null) {
                    logger.error("Got error", it)
                } else {
                    logger.info("Sent rubric")
                }
            }


            return@post call.respondText("Success", status = HttpStatusCode.OK)
        }
    }
}
