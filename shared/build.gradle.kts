import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("kotlinx-serialization")

    id("com.github.gmazzo.buildconfig")
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("de.jensklingenberg.ktorfit") version "1.0.0"
}

val ktorVersion = "2.3.2"
val ktorfitVersion = "1.5.0"

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
    version = ktorfitVersion
}

buildConfig {
    val getImgApiKey: String = gradleLocalProperties(rootDir).getProperty("getImgApiKey")

    className("ApiKeys")
    packageName("com.solanamobile.krate")

    buildConfigField("String", "GETIMG_API_KEY", "\"$getImgApiKey\"")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Krate root module"
        homepage = "https://solanamobile.com"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "shared"
            isStatic = true
        }

        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":profileScreen"))

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

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)

                implementation(libs.ktorfit.lib)

                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.kermit)
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

dependencies {
    val processor = "com.moriatsushi.koject:koject-processor-app:1.3.0"

    add("kspAndroid", processor)
    add("kspIosX64", processor)
    add("kspIosArm64", processor)
    add("kspIosSimulatorArm64", processor)

    implementation(libs.ktor.ktor.client.core)
    implementation(libs.ktor.ktor.client.cio)

    implementation(libs.okio)

    add("kspCommonMainMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosX64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosSimulatorArm64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.solanamobile.krate"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(11)
    }
}
