package com.solanamobile.krate

import androidx.compose.runtime.Composable
import com.solanamobile.krate.ui.AppScreenContent

@Composable
fun App() {
    PlatformInit()

    AppScreenContent()
}

@Composable
expect fun PlatformInit()