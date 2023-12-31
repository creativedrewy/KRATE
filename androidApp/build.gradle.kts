plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")

    id("com.google.gms.google-services")
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.solanamobile.krate"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.solanamobile.krate.KrateApplication"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    dependencies {
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
        implementation("androidx.compose.foundation:foundation:1.3.1")

        implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.android.gms:play-services-auth:20.7.0")

        implementation(libs.kotlin.reflect)
        implementation(libs.koject.android.core)
    }
}
