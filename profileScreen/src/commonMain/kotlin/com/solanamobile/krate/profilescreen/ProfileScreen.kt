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
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel
import com.solanamobile.krate.profilescreen.viewmodel.SomeState

object ProfileScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        ProfileScreenContent(
            state = state,
            onClick = {
                viewModel.modifyState()
            }
        )
    }

}

@Composable
fun ProfileScreenContent(
    state: SomeState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = onClick
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