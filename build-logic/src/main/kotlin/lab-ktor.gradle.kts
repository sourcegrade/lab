import io.ktor.plugin.features.DockerImageRegistry

plugins {
    id("io.ktor.plugin")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("sourcegrade/${project.name}")
        imageTag.set("latest")
        externalRegistry.set(
            DockerImageRegistry.externalRegistry(
                username = project.providers.gradleProperty("dockerRegistryUsername"),
                password = project.providers.gradleProperty("dockerRegistryPassword"),
                project = provider { project.name },
                hostname = provider { "images.sourcegrade.org" },
                namespace = provider { "sourcegrade" },
            ),
        )
    }
}
