package org.sourcegrade.lab.operator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.sourcegrade.lab.model.GraderLaunchOuterClass
import org.sourcegrade.lab.model.ProtobufContentConverter

fun main() {
    val client = HttpClient {
        install(ContentNegotiation) {
            register(ContentType.Application.ProtoBuf, ProtobufContentConverter())
        }
    }

    val launch = GraderLaunchOuterClass.GraderLaunch.newBuilder()
        .setLanguage("java")
        .setWorkDownload("some work")
        .setRubricWebhook("rubric web")
        .build()

    println("Got here")
    val response = runBlocking {
        client.post("http://localhost/launch") {
            contentType(ContentType.Application.ProtoBuf)
            setBody(launch)
            println("Sent content type: ${contentType()}")
        }
    }
    println("Response: $response")
}
