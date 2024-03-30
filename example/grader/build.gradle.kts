plugins {
    application
    id("lab-ktor")
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(project(":lab-model"))
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentnegotiation)
    implementation(libs.ktor.client.cio)
    implementation(libs.koin)
    implementation(libs.logging.core)
    implementation(libs.logging.slf4jimpl)
    implementation(libs.protobuf.kotlin)
}

application {
    mainClass.set("org.sourcegrade.lab.example.grader.MainKt")
}
