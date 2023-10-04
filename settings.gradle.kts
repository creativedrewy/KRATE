enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "JungleGym"

include(":androidApp")
include(":cameraScreen")
include(":chooserScreen")
include(":ComposePlaceholder")
include(":coroutines")
include(":createScreen")
include(":extension")
include(":krateDatabase")
include(":localStorage")
include(":profileScreen")
include(":underdogApi")
include(":shared")
include(":startScreen")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
