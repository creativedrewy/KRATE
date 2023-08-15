package com.solanamobile.krate.profilescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.moriatsushi.koject.compose.rememberInject
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel

object ProfileScreen: Screen {

    @Composable
    override fun Content() {
        ProfileScreenContent()
    }

}

@Composable
fun ProfileScreenContent(
    viewModel: ProfileScreenViewModel = rememberInject()
) {
    val state by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = {
                viewModel.modifyState()
            }
        ) {
            Text("Hello World")
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = state.myObj
        )
    }
}