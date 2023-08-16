package com.solanamobile.krate

import androidx.compose.runtime.Composable
import com.moriatsushi.koject.Koject
import com.moriatsushi.koject.start
import com.solanamobile.krate.ui.AppScreenContent

@Composable
fun App() {
    Koject.start()

    AppScreenContent()
}