plugins {
    application
    id("lab-ktor")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.graphql.server)
    implementation(libs.ktor.server.netty)
    implementation(libs.kubernetes.client)
}

application {
    mainClass.set("org.sourcegrade.lab.operator.MainKt")
}
