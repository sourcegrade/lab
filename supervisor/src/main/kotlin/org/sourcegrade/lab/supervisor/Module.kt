package org.sourcegrade.lab.supervisor

import com.expediagroup.graphql.server.operations.Query
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.sourcegrade.lab.model.ProtobufContentConverter
import java.util.UUID

class HelloWorldQuery : Query {
    fun hello(): String = "Hello World!"
}

fun Application.module(client: HttpClient, jobs: MutableMap<UUID, String>) {
//    install(Routing) {
//        graphQLPostRoute()
//    }

    install(Koin) {
        modules(org.koin.dsl.module { single<Logger> { LogManager.getLogger("SGL Operator") } })
    }

    install(ContentNegotiation) {
        register(ContentType.Application.ProtoBuf, ProtobufContentConverter)
    }

    routing {
        val logger by inject<Logger>()
        logger.info("Foo")
        post("/submit-rubric/{id}") {
            val id = call.parameters["id"]
            val rubric = call.receiveText() // TODO: Return real rubric
            logger.info("Received rubric with id $id")

            val uuid = UUID.fromString(id)

            jobs[uuid] = rubric

            logger.info("Finished with rubric $rubric")
        }
    }
}
