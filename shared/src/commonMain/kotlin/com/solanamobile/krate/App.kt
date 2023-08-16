package com.solanamobile.krate

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.start
import com.solanamobile.krate.createscreen.CreateScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    Koject.start()

    MaterialTheme {
        Navigator(
            screen = CreateScreen
        ) {
            SlideTransition(it)
        }
    }
}