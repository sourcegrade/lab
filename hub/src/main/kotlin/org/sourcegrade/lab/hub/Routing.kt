package org.sourcegrade.lab.hub

import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.request.uri
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.sourcegrade.lab.hub.http.UserSession

private suspend fun getSession(call: ApplicationCall): UserSession? {
    val userSession: UserSession? = call.sessions.get()
    // if there is no session, redirect to login
    if (userSession == null) {
        val redirectUrl =
            URLBuilder("http://0.0.0.0:8080/login").run {
                parameters.append("redirectUrl", call.request.uri)
                build()
            }
        call.respondRedirect(redirectUrl)
        return null
    }
    return userSession
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("Authentik", strategy = AuthenticationStrategy.Required) {
            get("/protected") {
                call.respondText("Hello Protected World!")
            }
        }
    }
}
