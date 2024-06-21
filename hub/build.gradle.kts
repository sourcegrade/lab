plugins {
    application
    id("lab-ktor")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.graphql.server)
    implementation(libs.koin)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.contentnegotiation)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.kubernetes.client)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logging.api)
    implementation(libs.logging.core)
    implementation(libs.logging.slf4jimpl)
    implementation(libs.ktor.server.cors)
    implementation(libs.exposed.core)
    implementation(libs.exposed.crypt)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.json)
    implementation(libs.postgresql)
    implementation(libs.bcrypt)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlin.reflect)
    implementation(libs.shiro)
}

application {
    mainClass.set("org.sourcegrade.lab.hub.MainKt")
}

tasks {
    named<JavaExec>("runShadow") {
        environment("SGL_DB_URL", "jdbc:postgresql://localhost:5432/sgl")
        environment("SGL_DB_USER", "admin")
        environment("SGL_DB_PASSWORD", "admin")
        environment("SGL_DEPLOYMENT_URL", "http://localhost:8080")
        environment("SGL_AUTH_URL", "http://localhost:8080/auth")
        environment("SGL_AUTH_CLIENT_ID", "sgl")
        environment("SGL_AUTH_CLIENT_SECRET", "sgl")
        environment("SGL_AUTH_ACCESS_TOKEN_URL", "http://localhost:8080/auth/token")
        environment("SGL_AUTH_SCOPES", "openid profile email")
    }
}
