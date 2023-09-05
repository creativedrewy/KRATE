plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")

    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposePlaceholder"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation("org.jetbrains.compose.ui:ui-util:1.5.0")
            }
        }

        val jvmMain by getting
    }
}

android {
    namespace = "com.solanamobile.placeholder"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}