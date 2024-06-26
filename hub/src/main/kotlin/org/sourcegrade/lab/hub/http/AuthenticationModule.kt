package org.sourcegrade.lab.hub.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
import io.ktor.server.routing.route
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
import org.sourcegrade.lab.hub.models.User
import org.sourcegrade.lab.hub.models.Users
import java.io.File
import kotlin.collections.set
import kotlin.time.Duration.Companion.hours
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun Application.authenticationModule() {
    val ktorEnv = environment

    val httpClient =
        HttpClient(CIO) {
            install(ClientContentNegotiation) {
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
            val url = ktorEnv.config.property("ktor.deployment.url").getString()

            urlProvider = { "$url$callback" }

            client = httpClient

            val oauthSettings =
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "Authentik",
                    authorizeUrl = ktorEnv.config.property("ktor.oauth.authorizeUrl")
                        .getString(),
                    accessTokenUrl = ktorEnv.config.property("ktor.oauth.accessTokenUrl")
                        .getString(),
                    requestMethod = HttpMethod.Post,
                    clientId = ktorEnv.config.property("ktor.oauth.clientId")
                        .getString(),
                    clientSecret = ktorEnv.config.property("ktor.oauth.clientSecret")
                        .getString(),
                    defaultScopes = ktorEnv.config.tryGetString("ktor.oauth.scopes")
                        ?.split(" ")
                        ?: listOf("openid", "profile", "email"),
                    onStateCreated = { call, state ->
                        // saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    },
                )
            providerLookup = { oauthSettings }
        }
    }
    routing {
        route("/api/session") {
            install(ServerContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
            authenticate("Authentik") {
                get("login") {
                    // Redirects to 'authorizeUrl' automatically
                }

                get("callback") {
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

                get("logout") {
                    call.sessions.clear<UserSession>()
                    call.respondRedirect("/")
                }
            }
            get("current-user") {
                withUser { call.respond(it.toDTO()) }
            }
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
data class UserSession(
    val userId: String,
    val state: String,
    val token: String,
    val email: String,
) : Principal

val redirects = mutableMapOf<String, String>()

@Serializable
data class OAuthUserInfo(
//    val id: String,
    val email: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("preferred_username") val preferredUsername: String,
    val groups: List<String>,
)
