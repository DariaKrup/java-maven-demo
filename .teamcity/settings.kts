import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import com.robomwm.samplejarprogram.Main

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

version = "2024.03"

project {

    buildType(Build)

    features {
        amazonEC2CloudImage {
            id = "PROJECT_EXT_38"
            profileId = "amazon-10"
            agentPoolId = "-2"
            name = "Ubuntu AWS Image"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            source = Source("ami-0817025aa39c203c6")
        }
        dockerRegistry {
            id = "PROJECT_EXT_40"
            name = "Docker Registry (Local)"
            userName = "dariakrup"
            password = "credentialsJSON:2f0d5011-031f-4b44-9f35-7a8c2d288465"
        }
        amazonEC2CloudProfile {
            id = "amazon-10"
            name = "Cloud AWS Profile"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = accessKey {
                keyId = "credentialsJSON:a778a904-1b51-41c6-906b-81e57b5c7a7e"
                secretKey = "credentialsJSON:ec56aca9-5346-4c26-b964-49b3a9384fc9"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    params {
        password("password_parameter", "credentialsJSON:02ec2742-e9b5-44f2-85d5-18b63a2202b3")
        text("text_parameter", Main.getConst())
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
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
