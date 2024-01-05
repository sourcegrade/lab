package org.sourcegrade.yougrade.hub.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.auth.session
import io.ktor.server.config.tryGetString
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.get
import io.ktor.server.sessions.maxAge
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.yougrade.hub.models.User
import org.sourcegrade.yougrade.hub.models.Users
import java.io.File
import kotlin.time.Duration.Companion.hours

fun Application.authenticationModule() {
    val httpClient =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = false
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    val callback = "/api/session/callback"

    install(Sessions) {
        cookie<UserSession>("X-SESSION-TOKEN", directorySessionStorage(File("sessions"), cached = false)) {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.maxAge = 1.hours
        }
    }
    install(Authentication) {
        session<UserSession>("internal-session") {
            validate { it }
            challenge {
                call.respondText("Not authenticated", status = HttpStatusCode.Unauthorized)
            }
        }
        // oidc authentication
        oauth("Authentik") {
            val url =
                this@authenticationModule.environment.config.tryGetString("ktor.deployment.url")
                    ?: throw IllegalStateException("Missing deployment url")

            urlProvider = { "$url$callback" }

            client = httpClient
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "Authentik",
                    authorizeUrl =
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.authorizeUrl")
                            ?: throw IllegalStateException("Missing OAuth authorize Url"),
                    accessTokenUrl =
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.accessTokenUrl")
                            ?: throw IllegalStateException("Missing OAuth access token Url"),
                    requestMethod = HttpMethod.Post,
                    clientId =
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.clientId")
                            ?: throw IllegalStateException("Missing OAuth client ID"),
                    clientSecret =
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.clientSecret")
                            ?: throw IllegalStateException("Missing OAuth client secret"),
                    defaultScopes =
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.scopes")
                            ?.split(" ")
                            ?: listOf("openid", "profile", "email"),
                    onStateCreated = { call, state ->
                        // saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    },
                )
            }
        }
    }
    routing {
        authenticate("Authentik") {
            get("/api/session/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get(callback) {
                val principal: OAuthAccessTokenResponse.OAuth2 = checkNotNull(call.principal()) { "No principal" }

                val userInfo =
                    httpClient.get(
                        this@authenticationModule.environment.config.tryGetString("ktor.oauth.userInfoUrl")
                            ?: throw IllegalStateException("Missing OAuth user info Url"),
                    ) {
                        header("Authorization", "Bearer ${principal.accessToken}")
                    }.body<OAuthUserInfo>()

                // find user in db

                val user =
                    newSuspendedTransaction {
                        User.find { Users.email eq userInfo.email }.firstOrNull()
                    } ?: newSuspendedTransaction {
                        User.new {
                            username = userInfo.preferredUsername
                            email = userInfo.email
                        }
                    }

                val session =
                    UserSession(
                        user.id.value,
                        checkNotNull(principal.state) { "No state" },
                        principal.accessToken,
                        userInfo.email,
                    )

                call.sessions.set(session)
                principal.state?.let { state ->
                    redirects[state]?.let { redirect ->
                        call.respondRedirect(redirect)
                        return@get
                    }
                }
                call.respondRedirect("/")
            }

            get("/api/session/logout") {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/")
            }
        }
        get("/api/session/current-user") {
            withUser { call.respond(it.toDTO()) }
        }
    }
}

suspend fun <T> PipelineContext<Unit, ApplicationCall>.withUserSession(block: suspend (UserSession) -> T): T? {
    val session = call.sessions.get<UserSession>()
    return if (session === null) {
        call.respondText("Not logged in", status = HttpStatusCode.Unauthorized)
        null
    } else {
        block(session)
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.withUser(block: suspend (User) -> Unit) {
    withUserSession { session ->
        val user = newSuspendedTransaction { User.findById(session.userId) }
        checkNotNull(user) { "Could not find user ${session.email} in DB" }
        block(user)
    }
}

@Serializable
public data class UserSession(
    val userId: String,
    val state: String,
    val token: String,
    val email: String,
) : Principal

public val redirects = mutableMapOf<String, String>()

@Serializable
data class OAuthUserInfo(
//    val id: String,
    val email: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("preferred_username") val preferredUsername: String,
    val groups: List<String>,
)
