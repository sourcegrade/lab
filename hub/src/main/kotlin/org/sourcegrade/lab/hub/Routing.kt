/*
 *   Lab - SourceGrade.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
