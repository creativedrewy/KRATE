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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.moriatsushi.koject.Qualifier
import com.moriatsushi.koject.compose.rememberInject
import com.solanamobile.krate.profilescreen.ProfileScreen
import com.solanamobile.krate.viewmodel.MainViewModel
import com.solanamobile.krate.viewmodel.ViewState
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
public inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
): T {
    val viewModel: T = rememberInject()

    return rememberScreenModel(tag = qualifier?.toString()) { viewModel }
}

object MainScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        MainContents(
            state = state,
            onClick = { txt ->
                viewModel.generateImageFromPrompt(txt)
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MainContents(
    state: ViewState,
    onClick: (text: String) -> Unit
) {
    var promptText by rememberSaveable { mutableStateOf("") }

    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigator.push(ProfileScreen)
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

//                LaunchedEffect(bmp) {
//                    imageBitmap = bmp
//                }

                imageBitmap?.let { myBmp ->
                    Image(
                        modifier = Modifier
                            .padding(
                                top = 16.dp
                            ),
                        bitmap = myBmp,
                        contentDescription = ""
                    )
                }
            }
            else -> {}
        }
    }
}