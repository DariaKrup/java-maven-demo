import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.Template
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot
import vars.teamcity.usage.ProjectName

object BookingApiPayconiq : Project({
    name = "BookingApiPayconiq"

    val mainProjectName = "${ProjectName}_Build_1"
    vcsRoot(BookingApiPayconiq_HttpsGithubComDariaKrupBookingApiPayconiqRefsHeadsMaster)

    template(BookingApiPayconiq_BuildConfig)
})

object BookingApiPayconiq_BuildConfig : Template({
    name = "Build_Config"
    artifactRules = "**/*=>archive.zip"

    vcs {
        root(BookingApiPayconiq_HttpsGithubComDariaKrupBookingApiPayconiqRefsHeadsMaster)
    }

    steps {
        script {
            name = "Output"
            id = "RUNNER_15"
            scriptContent = "echo 'booking subproject'"
        }
    }
})

object BookingApiPayconiq_HttpsGithubComDariaKrupBookingApiPayconiqRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/DariaKrup/BookingApiPayconiq#refs/heads/master"
    url = "https://github.com/DariaKrup/BookingApiPayconiq"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "DariaKrup"
        password = "credentialsJSON:63794333-2fd2-47da-8fd5-83b767905c78"
    }
})