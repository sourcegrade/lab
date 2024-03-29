package org.sourcegrade.lab.operator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.sourcegrade.lab.model.GraderLaunchOuterClass
import org.sourcegrade.lab.model.ProtobufContentConverter
import java.util.UUID

fun main() {
    val client = HttpClient {
        install(ContentNegotiation) {
            register(ContentType.Application.ProtoBuf, ProtobufContentConverter)
        }
    }

    // very rough idea

    val serverHost = "http://localhost:8081"
    val jobs = mutableMapOf<UUID, String>()
    val uuid = UUID.randomUUID()

    val launch = GraderLaunchOuterClass.GraderLaunch.newBuilder()
        .setLanguage("java")
        .setWorkDownload("some work")
        .setRubricWebhook("$serverHost/submit-rubric/$uuid")
        .build()

    val response = runBlocking {
        client.post("http://localhost:8080/launch") {
            contentType(ContentType.Application.ProtoBuf)
            setBody(launch)
        }
    }
    println("Sent grading request $response")
    if (response.status == HttpStatusCode.OK) {
        jobs[uuid] = "In progress"
        println("Grading request is ok")
    } else {
        error("Failed to start grader $response")
    }

    embeddedServer(Netty, port = 8081) { module(client, jobs) }
        .start(wait = true)
}
