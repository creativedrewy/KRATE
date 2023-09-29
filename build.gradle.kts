plugins {
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
    id("com.google.gms.google-services") version "4.3.15" apply false
}