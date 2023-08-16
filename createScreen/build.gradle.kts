import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlinx-serialization")

    id("com.github.gmazzo.buildconfig")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("de.jensklingenberg.ktorfit") version "1.0.0"
}

buildConfig {
    val getImgApiKey: String = gradleLocalProperties(rootDir).getProperty("getImgApiKey")

    className("ApiKeys")
    packageName("com.solanamobile.krate.createscreen")

    buildConfigField("String", "GETIMG_API_KEY", "\"$getImgApiKey\"")
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
            baseName = "createScreen"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.extension)
                implementation(projects.coroutines)

                implementation(libs.kotlinx.coroutines.core)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.koject.core)
                implementation(libs.koject.compose.core)

                implementation(libs.ktorfit.lib)

                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
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

val ktorfitVersion = "1.5.0"

dependencies {
    add("kspAndroid", libs.koject.processor.lib)
    add("kspIosX64", libs.koject.processor.lib)
    add("kspIosArm64", libs.koject.processor.lib)
    add("kspIosSimulatorArm64", libs.koject.processor.lib)

    implementation(libs.okio)

    add("kspCommonMainMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosX64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosSimulatorArm64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
}

android {
    namespace = "com.solanamobile.krate.createscreen"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }
}