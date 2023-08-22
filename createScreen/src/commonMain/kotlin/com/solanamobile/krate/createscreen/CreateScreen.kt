package com.solanamobile.krate.createscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.createscreen.viewmodel.CreateScreenViewModel
import com.solanamobile.krate.createscreen.viewmodel.ViewState
import com.solanamobile.krate.createscreen.viewmodel.isReady
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.getScreenModel
import kotlinx.coroutines.launch
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
            onSubmitPrompt = { txt ->
                viewModel.generateImageFromPrompt(txt)
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun CreateScreenContent(
    state: ViewState,
    resources: Map<String, ImageBitmap>,
    onSubmitPrompt: (text: String) -> Unit
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
                                navigator.push(ScreenRegistry.get(NavScreenProvider.ProfileScreen))
                            }
                        ) {
                            Image(
                                bitmap = resources["user"]!!,
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

            val scope = rememberCoroutineScope()
            val headingAnimation = remember { Animatable(0f) }

            val h1 = MaterialTheme.typography.h4
            val h2 = MaterialTheme.typography.h5

            val textStyle by remember(headingAnimation.value) {
                derivedStateOf {
                    lerp(h1, h2, headingAnimation.value)
                }
            }

            var moveHeadingPos = remember { mutableStateOf(false) }
            val topPadding = animateDpAsState(
                targetValue = if (!moveHeadingPos.value) 154.dp else 100.dp,
                animationSpec = tween(600)
            )

            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = topPadding.value
                    )
                    .onFocusEvent {
                        if (it.hasFocus) {
                            promptText = ""
                        }
                    }
                    .focusRequester(focusRequester),
                value = promptText,
                textStyle = textStyle,
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

            AnimatedContent(
                targetState = state
            ) { targetState ->
                when (targetState) {
                    ViewState.Prompting -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )

                            //TODO: Make this dock above the keyboard when typing
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
//                                    if (promptText != "What do you want to create today?") {
                                        focusManager.clearFocus()

                                        onSubmitPrompt(promptText)
//                                    }
                                }
                            ) {
                                Text(
                                    text = "CREATE",
                                    style = MaterialTheme.typography.h6
                                )
                            }
                        }
                    }
                    ViewState.Creating -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            resources.isReady {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = 20.dp,
                                            end = 20.dp,
                                            top = 67.dp
                                        )
                                        .fillMaxWidth()
                                ) {
                                    val starAnimPos by rememberInfiniteTransition().animateFloat(
                                        initialValue = -5f,
                                        targetValue = 10f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(350, easing = EaseInOutQuad),
                                            repeatMode = RepeatMode.Reverse
                                        )
                                    )

                                    val circleAnimPos by rememberInfiniteTransition().animateFloat(
                                        initialValue = -10f,
                                        targetValue = 10f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(400, easing = FastOutLinearInEasing),
                                            repeatMode = RepeatMode.Reverse
                                        )
                                    )

                                    val triangleAnimPos by rememberInfiniteTransition().animateFloat(
                                        initialValue = -5f,
                                        targetValue = 25f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(300, easing = EaseInOut),
                                            repeatMode = RepeatMode.Reverse
                                        )
                                    )

                                    Image(
                                        modifier = Modifier
                                            .padding(
                                                top = 25.dp,
                                                start = 31.dp
                                            )
                                            .offset(y = starAnimPos.dp)
                                            .size(114.dp),
                                        bitmap = resources["star"]!!,
                                        contentDescription = null
                                    )

                                    Image(
                                        modifier = Modifier
                                            .padding(
                                                top = 60.dp,
                                                start = 130.dp
                                            )
                                            .offset(y = circleAnimPos.dp)
                                            .size(84.dp),
                                        bitmap = resources["circle"]!!,
                                        contentDescription = null
                                    )

                                    Image(
                                        modifier = Modifier
                                            .padding(
                                                start = 200.dp
                                            )
                                            .offset(y = triangleAnimPos.dp)
                                            .size(100.dp),
                                        bitmap = resources["triangle"]!!,
                                        contentDescription = null
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 26.dp
                                    ),
                                text = "CREATING...",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                    is ViewState.Generated -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LaunchedEffect(Unit) {
                                //TODO: Make text input not editable in this state

                                launch {
                                    headingAnimation.animateTo(1f, tween(600))
                                }
                                launch {
                                    moveHeadingPos.value = true
                                }
                            }

                            val lazyListState = rememberLazyListState()
                            val snapBehavior = rememberSnapFlingBehavior(lazyListState)

                            LazyRow(
                                modifier = Modifier
                                    .padding(
                                        top = 56.dp
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(18.dp),
                                contentPadding = PaddingValues(
                                    start = 20.dp,
                                    end = 20.dp
                                ),
                                state = lazyListState,
                                flingBehavior = snapBehavior
                            ) {
                                items(listOf(4, 5, 6, 7)) {
                                    Box(
                                        modifier = Modifier
                                            .size(271.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.Red)
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            bitmap = resources[resources.keys.toList()[it]]!!,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }

                            OutlinedButton(
                                modifier = Modifier
                                    .padding(
                                        top = 16.dp
                                    ),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = MaterialTheme.colors.primary
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    MaterialTheme.colors.primary
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(12.dp),
                                onClick = { }
                            ) {
                                Text(
                                    text = "Save",
                                    style = MaterialTheme.typography.h6
                                )
                            }
                        }
                    }
                }
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