package com.solanamobile.krate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.moriatsushi.koject.compose.rememberInject
import com.solanamobile.krate.viewmodel.MainViewModel
import com.solanamobile.krate.viewmodel.ViewState
import org.jetbrains.compose.resources.ExperimentalResourceApi

object MainScreen: Screen {

    @Composable
    override fun Content() {
        MainContents()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainContents(
    viewModel: MainViewModel = rememberInject()
) {
    var promptText by remember { mutableStateOf("") }

    val state by viewModel.viewState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigator.push(ScreenTwo)
        }) {
            Text("Navigate to Next Screen")
        }

        TextField(
            value = promptText,
            label = {
                Text("Prompt (e.g. 'A dog wearing a hat')")
            },
            onValueChange = {
                promptText = it
            }
        )

        Button(onClick = {
            if (promptText.isNotBlank()) {
                viewModel.generateImageFromPrompt(promptText)
            }
        }) {
            Text("Generate Image")
        }

        when (state) {
            is ViewState.Loading -> {
                CircularProgressIndicator()
            }
            is ViewState.Generated -> {
                Image(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        ),
                    bitmap = (state as ViewState.Generated).bitmap,
                    contentDescription = ""
                )
            }
            else -> {}
        }
    }
}