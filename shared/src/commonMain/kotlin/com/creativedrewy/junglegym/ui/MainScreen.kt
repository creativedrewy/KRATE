package com.creativedrewy.junglegym.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.creativedrewy.junglegym.MainViewModel
import com.moriatsushi.koject.compose.rememberInject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
    var showImage by remember { mutableStateOf(false) }
    val state by viewModel.list.collectAsState()

    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            showImage = !showImage

            viewModel.doSomething()
        }) {
            Text(state)
        }

        Button(onClick = {
            navigator.push(ScreenTwo)
        }) {
            Text("Go to Next Screen")
        }

        AnimatedVisibility(showImage) {
            Image(
                painterResource("compose-multiplatform.xml"),
                null
            )
        }
    }
}