plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")

    id("com.google.devtools.ksp") version "1.9.0-1.0.13"

    id("app.cash.sqldelight") version "2.0.0"
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
            baseName = "kratedb"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koject.core)
                implementation(libs.koject.compose.core)

                implementation(libs.voyager.navigator)

                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.0")
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

            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.0.0")
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.solanamobile.krate.kratedb")
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
    namespace = "com.solanamobile.krate.kratedb"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}