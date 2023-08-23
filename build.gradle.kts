plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

buildscript {
    dependencies {
        classpath("io.github.skeptick.libres:gradle-plugin:1.2.0-beta01")
    }
}