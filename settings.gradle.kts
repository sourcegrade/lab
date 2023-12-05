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

rootProject.name = "autogd"

sequenceOf(
    "operator",
    "hub",
).forEach {
    val project = ":autogd-$it"
    include(project)
    project(project).projectDir = file(it)
}
