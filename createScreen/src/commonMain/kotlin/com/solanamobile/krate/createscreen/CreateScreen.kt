package com.solanamobile.krate.createscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.createscreen.viewmodel.CreateScreenViewModel
import com.solanamobile.krate.createscreen.viewmodel.ViewState
import com.solanamobile.krate.createscreen.viewmodel.isReady
import com.solanamobile.krate.extension.getScreenModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

class CreateScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()
        val resources = viewModel.resources.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadResources()
        }

        CreateScreenContent(
            state,
            resources,
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
    resources: List<ImageBitmap>,
    onClick: (text: String) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(
                top = 44.dp
            ),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = { },
                navigationIcon = { },
                actions = {
                    resources.isReady {
                        Button(
                            modifier = Modifier
                                .padding(
                                    end = 20.dp
                                )
                                .size(40.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ),
                            onClick = {

                            }
                        ) {
                            Image(
                                bitmap = resources[0],
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var promptText by rememberSaveable { mutableStateOf("What do you want to create today?") }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 154.dp
                    )
                    .onFocusEvent {
                        if (it.hasFocus) {
                            promptText = ""
                        }
                    },
                value = promptText,
                textStyle = MaterialTheme.typography.h4,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFFF06441),
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    promptText = it
                }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Button(
                modifier = Modifier
                    .padding(
                        bottom = 30.dp
                    )
                    .size(84.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ),
                onClick = {

                }
            ) {
                Text(
                    text = "CREATE",
                    style = MaterialTheme.typography.h5
                )
            }
//            when (state) {
//                is ViewState.Loading -> {
//                    CircularProgressIndicator()
//                }
//                is ViewState.Generated -> {
//                    val bmp = state.bitmap
//                    var imageBitmap by remember(bmp) { mutableStateOf(bmp) }
//
//                    imageBitmap?.let { myBmp ->
//                        Image(
//                            modifier = Modifier
//                                .padding(
//                                    top = 16.dp
//                                )
//                                .width(300.dp)
//                                .height(300.dp)
//                                .background(Color.LightGray),
//                            bitmap = myBmp,
//                            contentScale = ContentScale.None,
//                            alignment = Alignment.Center,
//                            contentDescription = ""
//                        )
//                    }
//                }
//                else -> {}
//            }
        }
    }
}