plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("io.ktor.plugin:plugin:${libs.plugins.ktor.get().version}")
}
