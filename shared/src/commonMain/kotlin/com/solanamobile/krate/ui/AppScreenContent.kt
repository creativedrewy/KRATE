package com.solanamobile.krate.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.solanamobile.krate.camerascreen.CameraScreen
import com.solanamobile.krate.createscreen.CreateScreen
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.compositionlocal.ProvideResourceLocator
import com.solanamobile.krate.profilescreen.ProfileScreen
import com.solanamobile.krate.startscreen.StartScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScreenContent() {
    ScreenRegistry {
        register<NavScreenProvider.StartScreen> {
            StartScreen()
        }
        register<NavScreenProvider.CameraScreen> {
            CameraScreen()
        }
        register<NavScreenProvider.CreateScreen> {
            CreateScreen(
                createMode = it.createMode,
                userImage = it.userImage
            )
        }
        register<NavScreenProvider.ProfileScreen> {
            ProfileScreen
        }
    }



    KrateAppTheme {
        ProvideResourceLocator(
            locator = resLocator
        ) {
            Navigator(
                screen = ScreenRegistry.get(NavScreenProvider.StartScreen)
            ) {
                SlideTransition(it)
            }
        }
    }
}