package com.solanamobile.krate.extension

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class NavScreenProvider: ScreenProvider {
    object StartScreen: NavScreenProvider()
    object CameraScreen: NavScreenProvider()
    object CreateScreen: NavScreenProvider()
    object ProfileScreen: NavScreenProvider()
}