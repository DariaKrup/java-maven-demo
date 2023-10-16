import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.projectFeatures.hashiCorpVaultParameter
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter
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

version = "2023.05"

project {

    buildType(Build)

    features {
        awsConnection {
            id = "AmazonWebServicesAws"
            name = "Amazon Web Services (AWS)"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:ec56aca9-5346-4c26-b964-49b3a9384fc9"
                stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
            }
        }
        dockerRegistry {
            id = "PROJECT_EXT_10"
            name = "Docker Registry Local"
            userName = "dariakrup"
            password = "credentialsJSON:f99d2d9f-043c-428d-a9a3-1f63102c0029"
        }
        hashiCorpVaultParameter {
            id = "PROJECT_EXT_11"
            name = "HashiCorp Vault IAM"
            vaultNamespace = "auth/aws"
            url = "https://vault.burnasheva.click:8200/"
            authMethod = iam()
        }
        hashiCorpVaultParameter {
            id = "PROJECT_EXT_9"
            name = "HashiCorp Vault Local"
            url = "https://vault.burnasheva.click:8200/"
            authMethod = appRole {
                roleId = "23b3c7b7-40ef-086b-e5eb-276aaec9c43a"
                secretId = "credentialsJSON:75fdee81-80da-47b1-8375-2c446fb27922"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    params {
        password("password_parameter", "credentialsJSON:27a44fd6-e392-410a-b15d-f0488c67be9a")
        text("text_parameter", "text_value", allowEmpty = true)
        hashiCorpVaultParameter {
            name = "github_token"
            query = "passwords_storage_v1/github!/token"
        }
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
        script {
            name = "Parameter to file"
            id = "Parameter_to_file"
            scriptContent = "echo %github_token% >> token.txt"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
