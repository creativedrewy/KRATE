package com.solanamobile.krate.createscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.createscreen.viewmodel.CreateScreenViewModel
import com.solanamobile.krate.createscreen.viewmodel.ViewState
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.getScreenModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

class CreateScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        CreateScreenContent(
            state,
            onClick = { txt ->
                viewModel.generateImageFromPrompt(txt)
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreateScreenContent(
    state: ViewState,
    onClick: (text: String) -> Unit
) {
    var promptText by rememberSaveable { mutableStateOf("") }

    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigator.push(ScreenRegistry.get(NavScreenProvider.ProfileScreen))
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
                onClick(promptText)
            }
        }) {
            val text = if (state is ViewState.Generated) {
                "You have generated an image!!"
            } else {
                "Generate Image"
            }

            Text(text)
        }

        when (state) {
            is ViewState.Loading -> {
                CircularProgressIndicator()
            }
            is ViewState.Generated -> {
                val bmp = state.bitmap
                var imageBitmap by remember(bmp) { mutableStateOf(bmp) }

                imageBitmap?.let { myBmp ->
                    Image(
                        modifier = Modifier
                            .padding(
                                top = 16.dp
                            )
                            .width(300.dp)
                            .height(300.dp)
                            .background(Color.LightGray),
                        bitmap = myBmp,
                        contentScale = ContentScale.None,
                        alignment = Alignment.Center,
                        contentDescription = ""
                    )
                }
            }
            else -> {}
        }
    }
}