package com.solanamobile.krate

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.start
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
fun MainViewController(createUIView: () -> Unit) = ComposeUIViewController {
    createUIView()

    App()
}

@Composable
actual fun PlatformInit() {
    Koject.start()
}