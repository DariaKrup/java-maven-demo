import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
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

version = "2024.07"

project {

    buildType(Build)

    features {
        awsConnection {
            id = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsIam"
            name = "Amazon Web Services (AWS): IAM"
            regionName = "eu-west-1"
            credentialsType = iamRole {
                roleArn = "arn:aws:iam::913206223978:role/dkrupkinaEc2Role"
                awsConnectionId = "AmazonWebServicesAws_2"
                stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
            }
            allowInBuilds = false
        }
        awsConnection {
            id = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsKeys"
            name = "Amazon Web Services (AWS): keys"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVI62P5XDY"
                secretAccessKey = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
                stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
            }
            allowInSubProjects = true
            allowInBuilds = true
        }
        awsConnection {
            id = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsProviderChain"
            name = "Amazon Web Services (AWS): Provider Chain"
            regionName = "eu-west-1"
            credentialsType = default()
            allowInBuilds = false
            param("awsStsEndpoint", "https://sts.eu-west-1.amazonaws.com")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_34"
            profileId = "amazon-20"
            agentPoolId = "-2"
            imagePriority = 10
            name = "Ubuntu AMI"
            vpcSubnetId = "subnet-0ace2a91ee63119ea,subnet-043178c302cabfe37"
            iamProfile = "dkrupkinaEc2Role"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            instanceTags = mapOf(
                "Owner" to "daria.krupkina@jetbrains.com"
            )
            maxInstancesCount = 3
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_35"
            profileId = "amazon-21"
            agentPoolId = "-2"
            imagePriority = 3
            name = "Instance Image"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("i-0aa8f308327fd1bc1")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_36"
            profileId = "amazon-22"
            imagePriority = 1
            name = "Spot Fleet request"
            source = SpotFleetConfig("""
                {
                                    "IamFleetRole": "arn:aws:iam::913206223978:role/aws-ec2-spot-fleet-tagging-role",
                                    "AllocationStrategy": "priceCapacityOptimized",
                                    "TargetCapacity": 3,
                                    "ValidFrom": "2024-05-03T09:06:36.000Z",
                                    "ValidUntil": "2025-05-03T09:06:36.000Z",
                                    "TerminateInstancesWithExpiration": true,
                                    "Type": "request",
                                    "TargetCapacityUnitType": "units",
                                    "LaunchSpecifications": [
                                        {
                                            "ImageId": "ami-0817025aa39c203c6",
                                            "KeyName": "daria.krupkina",
                                            "BlockDeviceMappings": [
                                                {
                                                    "DeviceName": "/dev/sda1",
                                                    "Ebs": {
                                                        "DeleteOnTermination": true,
                                                        "SnapshotId": "snap-08e52b439cb6eade3",
                                                        "VolumeSize": 16,
                                                        "VolumeType": "gp2",
                                                        "Encrypted": false
                                                    }
                                                },
                                                {
                                                    "DeviceName": "/dev/sdb",
                                                    "VirtualName": "ephemeral0",
                                                    "Ebs": {}
                                                },
                                                {
                                                    "DeviceName": "/dev/sdc",
                                                    "VirtualName": "ephemeral1",
                                                    "Ebs": {}
                                                }
                                            ],
                                            "SubnetId": "subnet-0ace2a91ee63119ea",
                                            "InstanceRequirements": {
                                                "VCpuCount": {
                                                    "Min": 1,
                                                    "Max": 4
                                                },
                                                "MemoryMiB": {
                                                    "Min": 0,
                                                    "Max": 4096
                                                }
                                            },
                                            "TagSpecifications": [
                                                    {
                                                        "ResourceType": "instance",
                                                        "Tags": [
                                                            {
                                                                "Key": "Owner",
                                                                "Value": "daria.krupkina@jetbrains.com"
                                                            }
                                                        ]
                                                    }
                                                ]
                                        }
                                    ],
                                   "TagSpecifications": [
                                            {
                                                "ResourceType": "spot-fleet-request",
                                                "Tags": [
                                                    {
                                                        "Key": "Owner",
                                                        "Value": "daria.krupkina@jetbrains.com"
                                                    }
                                                ]
                                            }        
                                   ]
                                }
            """.trimIndent())
        }
        amazonEC2CloudProfile {
            id = "amazon-20"
            name = "AWS EC2: IAM"
            description = "AWS EC2 profile with IAM role authentication."
            serverURL = "http://10.128.93.57:8281/"
            terminateAfterBuild = true
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsIam"
            maxInstancesCount = 5
        }
        amazonEC2CloudProfile {
            id = "amazon-21"
            name = "AWS EC2: Provider Chain"
            description = "AWS EC2 Cloud Profile with Provider chain authentication."
            terminateIdleMinutes = 0
            terminateBeforeFullHourMinutes = 15
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsProviderChain"
        }
        amazonEC2CloudProfile {
            id = "amazon-22"
            name = "AWS EC2: keys"
            description = "AWS EC2 Cloud Profile with access keys authentication."
            terminateIdleMinutes = 0
            terminateTotalWorkMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AwsEc2Profile_JavaMavenDemo_AmazonWebServicesAwsKeys"
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
