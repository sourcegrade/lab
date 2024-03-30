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

rootProject.name = "lab"

sequenceOf(
    "example-grader",
    "hub",
    "model",
    "operator",
    "supervisor",
).forEach {
    val project = ":lab-$it"
    include(project)
    project(project).projectDir = file(it.replace('-', '/'))
}
