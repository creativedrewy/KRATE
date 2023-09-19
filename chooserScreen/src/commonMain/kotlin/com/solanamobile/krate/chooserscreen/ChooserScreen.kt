package com.solanamobile.krate.chooserscreen

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ChooserScreen: Screen {

    @Composable
    override fun Content() {
        ChooserScreenContent()
    }

}

@Composable
fun ChooserScreenContent() {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background
    ) {

    }
}