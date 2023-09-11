dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "hub"

sequenceOf(
    "backend",
    "frontend",
).forEach {
    val project = ":hub-$it"
    include(project)
    project(project).projectDir = file(it)
}
