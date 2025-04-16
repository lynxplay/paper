import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

if (!file(".git").exists()) {
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Paper project directory is not a properly cloned Git repository.
         
         In order to build Paper from source you must clone
         the Paper repository using Git, not download a code
         zip from GitHub.
         
         Built Paper jars are available for download at
         https://papermc.io/downloads/paper
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper.
        ===================================================
    """.trimIndent()
    error(errorText)
}

rootProject.name = "paper"

for (name in listOf("paper-api", "paper-server")) {
    include(name)
    file(name).mkdirs()
}

optionalInclude("test-plugin")
optionalInclude("paper-generator")
optionalInclude(
    "shelfback",
    paths = listOf("shelfback:shelfback-api", "shelfback:shelfback-runner", "shelfback:shelfback-tests")
)

fun optionalInclude(name: String, op: (ProjectDescriptor.() -> Unit)? = null, paths: List<String> = listOf(name)) {
    val settingsFile = file("$name.settings.gradle.kts")
    if (settingsFile.exists()) {
        apply(from = settingsFile)
        findProject(":$name")?.let { op?.invoke(it) }
    } else {
        val content = StringBuilder()
        content.append(
            """
            // Uncomment to enable the '$name' module
            // includeThisModule()
            
            fun includeThisModule() {

            """.trimIndent()
        )
        content.append("  ").append(paths.joinToString("\n  ") { it -> "include(\":$it\")" })
        content.append("\n}")
        settingsFile.writeText(content.toString())
    }
}
