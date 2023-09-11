import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow)
    alias(libs.plugins.style)
}

val projectVersion = file("version").readLines().first()
project.extra["apiVersion"] = projectVersion.replace("\\.[1-9]\\d*-SNAPSHOT|\\.0|\\.\\d*\$".toRegex(), "")

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.sourcegrade.style")

    group = "org.sourcegrade"
    version = projectVersion

    project.findProperty("buildNumber")
        ?.takeIf { version.toString().contains("SNAPSHOT") }
        ?.also { version = version.toString().replace("SNAPSHOT", "RC$it") }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "11"
        }
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = "11"
            targetCompatibility = "11"
        }
    }
}
