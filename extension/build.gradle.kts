plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlinx-serialization")

    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "extension"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(compose.runtime)
                implementation(compose.foundation)

                implementation(libs.koject.core)
                implementation(libs.koject.compose.core)

                implementation(libs.voyager.navigator)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.activity.compose)
                api(libs.core.ktx)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

dependencies {
    add("kspAndroid", libs.koject.processor.lib)
    add("kspIosX64", libs.koject.processor.lib)
    add("kspIosArm64", libs.koject.processor.lib)
    add("kspIosSimulatorArm64", libs.koject.processor.lib)
}

android {
    namespace = "com.solanamobile.krate.extension"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}