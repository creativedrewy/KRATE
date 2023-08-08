package com.creativedrewy.junglegym

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.creativedrewy.junglegym.ui.MainScreen
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.start

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    Koject.start()

    MaterialTheme {
        Navigator(
            screen = MainScreen
        ) {
            SlideTransition(it)
        }
    }
}