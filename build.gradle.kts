import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow)
    alias(libs.plugins.ktlint)
}

val projectVersion = file("version").readLines().first()

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    group = "org.sourcegrade"
    version = projectVersion

    project.findProperty("buildNumber")
        ?.takeIf { version.toString().contains("SNAPSHOT") }
        ?.also { version = version.toString().replace("SNAPSHOT", "RC$it") }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = "17"
            targetCompatibility = "17"
        }
    }
}

tasks {
    register("initWorkspace") {
        dependsOn(":lab-model:generateProto")
    }
}
