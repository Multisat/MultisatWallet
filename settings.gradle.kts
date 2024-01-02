import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }

        val properties = Properties()
        properties.load(FileInputStream("local.properties"))

        maven {
            url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")
            credentials {
                username = properties.getProperty("githubUser")
                password = properties.getProperty("githubToken")
            }
        }
    }
}

rootProject.name = "Multisat Wallet"
include(":app")
 