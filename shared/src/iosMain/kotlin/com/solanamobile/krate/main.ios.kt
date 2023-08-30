package com.solanamobile.krate

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.start

fun MainViewController() = ComposeUIViewController { App() }

@Composable
actual fun PlatformInit() {
    Koject.start()
}