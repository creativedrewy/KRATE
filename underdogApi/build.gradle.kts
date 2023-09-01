import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.konan.properties.hasProperty

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")

    id("com.github.gmazzo.buildconfig")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("de.jensklingenberg.ktorfit") version "1.0.0"
}

buildConfig {
    if (gradleLocalProperties(rootDir).hasProperty("underdogApiKey")) {
        val underdogApiKey: String = gradleLocalProperties(rootDir).getProperty("underdogApiKey")

        className("ApiKeys")
        packageName("com.underdogprotocol.api")

        buildConfigField("String", "API_KEY", "\"$underdogApiKey\"")
    }
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

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "UnderdogApi"
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktorfit.lib)

                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        val jvmMain by getting
    }
}

dependencies {
    val ktorfitVersion = "1.5.0"

    add("kspCommonMainMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosX64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosSimulatorArm64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
}

android {
    namespace = "com.underdogprotocol.api"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}