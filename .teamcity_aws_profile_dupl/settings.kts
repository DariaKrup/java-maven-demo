import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesExecutor
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

version = "2025.03"

project {

    buildType(Build)

    features {
        kubernetesExecutor {
            id = "K8S_EXECUTOR"
            connectionId = "PROJECT_EXT_2"
            profileName = "K8S"
            serverURL = "http://10.128.93.57:8281/"
            templateName = "aws-certficate-agent"
        }
        amazonEC2CloudProfile {
            id = "amazon-21"
            name = "EC2"
            serverURL = "http://10.128.93.57:8281/"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AmazonWebServicesAws_2"
        }
        awsConnection {
            id = "awsConnection_5"
            name = "Amazon Web Services (AWS, DCPC)"
            regionName = "eu-west-1"
            credentialsType = default()
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        awsConnection {
            id = "awsConnection_6"
            name = "Amazon Web Services (AWS): IAM"
            regionName = "eu-west-1"
            credentialsType = iamRole {
                roleArn = "arn:aws:iam::913206223978:role/dkrupkinaEc2Role"
                awsConnectionId = "AmazonWebServicesAws_2"
            }
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        awsConnection {
            id = "awsConnection_7"
            name = "Amazon Web Services (AWS): keys"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
            }
            allowInSubProjects = true
            allowInBuilds = true
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        awsConnection {
            id = "awsConnection_8"
            name = "Amazon Web Services (AWS): Provider Chain"
            regionName = "eu-west-1"
            credentialsType = default()
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
    }
}

object Build : BuildType({
    name = "Build"

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
