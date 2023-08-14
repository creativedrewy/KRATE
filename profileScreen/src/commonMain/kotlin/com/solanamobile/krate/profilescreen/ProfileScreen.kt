package com.solanamobile.krate.profilescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object ProfileScreen: Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            val navigator = LocalNavigator.currentOrThrow

            Button(
                onClick = {
                    navigator.pop()
                }
            ) {
                Text("Hello World")
            }
        }
    }

}