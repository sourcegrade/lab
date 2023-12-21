plugins {
    id("autogd-ktor")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.graphql.server)
    implementation(libs.koin)
    implementation(libs.ktor.server.netty)
    implementation(libs.kubernetes.client)
    implementation(libs.logging.core)
    runtimeOnly(libs.logging.slf4jimpl)
}
