package com.solanamobile.krate.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.solanamobile.krate.createscreen.CreateScreen
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.profilescreen.ProfileScreen
import com.solanamobile.krate.startscreen.StartScreen

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

    MaterialTheme {
        Navigator(
            screen = ScreenRegistry.get(NavScreenProvider.StartScreen)
        ) {
            SlideTransition(it)
        }
    }
}