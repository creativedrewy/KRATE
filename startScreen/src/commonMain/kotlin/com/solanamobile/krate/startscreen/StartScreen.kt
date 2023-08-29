package com.solanamobile.krate.startscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.compositionlocal.LocalResourceLocator
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.extension.ui.ResourceImage
import com.solanamobile.krate.startscreen.ui.AnimatedArrows
import com.solanamobile.krate.startscreen.viewmodel.StartScreenViewModel
import com.solanamobile.krate.startscreen.viewmodel.StartScreenViewState
import kotlinx.coroutines.launch

class StartScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: StartScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(
                top = 183.dp,
                start = 20.dp,
                end = 20.dp
            )
    ) {
        Text(
            text = "CREATORS\nGONNA",
            color = Color(0xFF172C4A),
            style = MaterialTheme.typography.h2.copy(
                drawStyle = Stroke(
                    miter = 10f,
                    width = 5f,
                    join= StrokeJoin.Miter
                )
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ResourceImage(
                resourceName = "krate0.png"
            )

            ResourceImage(
                modifier = Modifier
                    .padding(
                        bottom = 33.dp
                    ),
                resourceName = "krate1.png"
            )

            ResourceImage(
                modifier = Modifier
                    .padding(
                        bottom = 66.dp
                    ),
                resourceName = "krate2.png"
            )

            ResourceImage(
                modifier = Modifier
                    .padding(
                        bottom = 99.dp
                    ),
                resourceName = "krate3.png"
            )

            ResourceImage(
                modifier = Modifier
                    .padding(
                        bottom = 132.dp
                    ),
                resourceName = "krate4.png"
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .padding(
                    bottom = 26.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        navigator.replace(ScreenRegistry.get(NavScreenProvider.CreateScreen))
                    },
                text = "KRATE NOW",
                style = MaterialTheme.typography.h3,
                fontSize = 32.sp,
                lineHeight = 60.sp,
            )

            AnimatedArrows()
        }
    }
}