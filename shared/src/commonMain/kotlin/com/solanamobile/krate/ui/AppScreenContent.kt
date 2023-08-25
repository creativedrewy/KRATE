package com.solanamobile.krate.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.solanamobile.krate.Res
import com.solanamobile.krate.createscreen.CreateScreen
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.compositionlocal.ProvideResourceLocator
import com.solanamobile.krate.extension.compositionlocal.ResourceLocator
import com.solanamobile.krate.profilescreen.ProfileScreen
import com.solanamobile.krate.startscreen.StartScreen
import io.github.skeptick.libres.images.Image

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScreenContent() {
    ScreenRegistry {
        register<NavScreenProvider.StartScreen> {
            StartScreen()
        }
        register<NavScreenProvider.CreateScreen> {
            CreateScreen()
        }
        register<NavScreenProvider.ProfileScreen> {
            ProfileScreen
        }
    }

    val resLocator = object : ResourceLocator {
        override fun getResource(name: String): Image {
            return Res.image.icon_caret_fill
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