dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
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

rootProject.name = "yougrade"

sequenceOf(
    "operator",
    "hub",
).forEach {
    val project = ":yougrade-$it"
    include(project)
    project(project).projectDir = file(it)
}
