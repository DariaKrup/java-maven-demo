
import jetbrains.buildServer.configs.kotlin.BuildType
import lib.teamcity.CategoryName
import lib.teamcity.ProjectName


object Build : BuildType({
    name = "Build"
    val projectId = "${CategoryName}_${ProjectName}_Build_Project"
})
