plugins {
    application
    id("lab-ktor")
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.graphql.server)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kubernetes.client)
    implementation(libs.ktor.serialisation.kotlinx.json)
    implementation(libs.logging.api)
    implementation(libs.logging.core)
    implementation(libs.logging.impl)
    implementation(libs.ktor.server.cors)
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")
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
}

application {
    mainClass.set("org.sourcegrade.lab.hub.MainKt")
}
