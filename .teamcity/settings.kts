import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
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

    params {
        text("parameter_text_value", "value", allowEmpty = true)
    }

    features {
        amazonEC2CloudImage {
            id = "PROJECT_EXT_10"
            profileId = "amazon-8"
            agentPoolId = "21"
            imagePriority = 5
            name = "Ubuntu Image"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_11"
            profileId = "amazon-8"
            agentPoolId = "21"
            imagePriority = 3
            name = "Ubuntu Image in another subnet"
            vpcSubnetId = "subnet-0ace2a91ee63119ea"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            maxInstancesCount = 5
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_12"
            profileId = "amazon-8"
            agentPoolId = "21"
            imagePriority = 5
            name = "Ubuntu Image (1)"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            maxInstancesCount = 3
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_14"
            profileId = "amazon-8"
            agentPoolId = "21"
            imagePriority = 9
            name = "Ubuntu Image Highest"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "c4.large"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            maxInstancesCount = 2
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_15"
            profileId = "amazon-8"
            agentPoolId = "-2"
            imagePriority = 10
            name = "Ubuntu Image Highest c4 copy"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "c4.large"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            maxInstancesCount = 2
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_16"
            profileId = "amazon-8"
            imagePriority = 10
            name = "C4 Image"
            vpcSubnetId = "subnet-0ace2a91ee63119ea"
            keyPairName = "daria.krupkina"
            instanceType = "c4.xlarge"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
            param("amazon-name", "simple-ubuntu-agent-with-pwsh")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_17"
            profileId = "amazon-8"
            agentPoolId = "-2"
            imagePriority = 10
            name = "C4 Image Copy"
            vpcSubnetId = "subnet-043178c302cabfe37"
            keyPairName = "daria.krupkina"
            instanceType = "c4.xlarge"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudProfile {
            id = "amazon-8"
            name = "Cloud AWS EC2 Profile"
            serverURL = "http://10.128.93.51:8181"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = accessKey {
                keyId = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
                secretKey = "credentialsJSON:b8284969-a0a5-4b40-8e3c-5e024c68c682"
            }
        }
    }
}

object Build : BuildType({
    name = "Build_New"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
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
