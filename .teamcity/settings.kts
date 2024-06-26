import jetbrains.buildServer.configs.kotlin.BuildFeatures
import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.DslContext
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.project
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubIssues
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

project {

    val test = Test()
    val style = Style()

    buildType(test)
    buildType(style)

    features {
        githubIssues {
            id = "PROJECT_EXT_3"
            displayName = "sourcegrade/lab"
            repositoryURL = "https://github.com/sourcegrade/lab"
            authType = accessToken {
                accessToken = "credentialsJSON:7828090f-5bd5-448c-99df-b7fae9540192"
            }
            param("tokenId", "")
        }
    }
}

fun BuildType.configureVcs() {
    vcs {
        root(DslContext.settingsRoot)
    }
}

fun BuildType.configureTriggers() {
    triggers {
        vcs {
            triggerRules = """
                +:**.java
                +:**.kt
                +:**.kts
                -:comment=^\\[ci skip\\].*
            """.trimIndent()
            branchFilter = "+:*"
        }
    }
}

fun BuildFeatures.configureBaseFeatures() {
    perfmon {}
    commitStatusPublisher {
        vcsRootExtId = "${DslContext.settingsRoot.id}"
        publisher = github {
            githubUrl = "https://api.github.com"
            authType = personalToken {
                token = "credentialsJSON:7828090f-5bd5-448c-99df-b7fae9540192"
            }
        }
    }
}

class Test : BuildType() {
    init {
        name = "test"

        configureVcs()
        configureTriggers()
        features {
            configureBaseFeatures()
        }

        steps {
            gradle {
                id = "gradle_runner"
                tasks = "test"
                gradleParams = "--refresh-dependencies"
            }
        }
    }
}

class Style : BuildType() {
    init {
        name = "style"

        configureVcs()
        configureTriggers()
        features {
            configureBaseFeatures()
        }

        steps {
            gradle {
                id = "gradle_runner"
                tasks = "ktlintCheck"
            }
        }
    }
}
