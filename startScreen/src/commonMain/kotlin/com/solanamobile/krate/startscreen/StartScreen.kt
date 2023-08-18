package com.solanamobile.krate.startscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.startscreen.viewmodel.StartScreenViewModel
import com.solanamobile.krate.startscreen.viewmodel.StartScreenViewState

class StartScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: StartScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadLogos()
        }

        StartScreenContents(
            viewState = state
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun StartScreenContents(
    viewState: StartScreenViewState
) {
    val navigator = LocalNavigator.currentOrThrow

//        val scope = rememberCoroutineScope()
//
//        scope.launch {
//            delay(500)
//
//            navigator.replace(ScreenRegistry.get(NavScreenProvider.CreateScreen))
//        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(
                top = 183.dp,
                start = 24.dp,
//                    end = 24.dp
            )
    ) {
        Text(
            text = "CREATORS\nGONNA",
            color = Color(0xFF172C4A),
            style = TextStyle.Default.copy(
                fontSize = 59.sp,
                lineHeight = 60.sp,
                drawStyle = Stroke(
                    miter = 10f,
                    width = 5f,
                    join= StrokeJoin.Miter
                )
            )
        )
    }
}