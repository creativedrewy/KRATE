plugins {
    kotlin("multiplatform")
    id("com.android.library")

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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "localStorage"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.extension)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.koject.core)
                implementation(libs.koject.compose.core)

                implementation(libs.kermit)

                implementation(libs.solanaeddsa)
                implementation(libs.solanainterfaces)
                implementation(libs.solanapublickeys)

                implementation("com.russhwolf:multiplatform-settings-no-arg:1.0.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
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
    namespace = "com.solanamobile.krate.localstorage"
    compileSdk = 33

    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 24
    }
}