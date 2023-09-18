package com.solanamobile.krate.extension

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class NavScreenProvider: ScreenProvider {
    object StartScreen: NavScreenProvider()

    object CameraScreen: NavScreenProvider()

    enum class CreateMode {
        Teleport, Text2Img
    }

    class CreateScreen(
        val createMode: CreateMode,
        val userImage: ImageBitmap? = null
    ): NavScreenProvider()

    object ProfileScreen: NavScreenProvider()
}