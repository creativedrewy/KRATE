package com.solanamobile.krate.createscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.createscreen.viewmodel.CreateScreenViewModel
import com.solanamobile.krate.createscreen.viewmodel.ViewState
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.extension.ui.ResourceImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class CreateScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: CreateScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        CreateScreenContent(
            state,
            onSubmitPrompt = { txt ->
                viewModel.generateImageFromPrompt(txt)
            },
            onResetState = {
                viewModel.resetState()
            },
            onSavePhoto = { index ->
                viewModel.saveToPhotos(index)
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)
@Composable
fun CreateScreenContent(
    state: ViewState,
    onSubmitPrompt: (text: String) -> Unit = { },
    onResetState: () -> Unit = { },
    onSavePhoto: (Int) -> Unit = { }
) {
    val navigator = LocalNavigator.currentOrThrow

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val pagerState = rememberPagerState(
        pageCount = { (state as? ViewState.Generated)?.images?.size ?: 0 }
    )

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 22.dp,
                            bottom = 28.dp
                        ),
                    text = "Save your creation",
                    color = MaterialTheme.colors.onSurface
                )

//                Divider(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    thickness = 1.dp,
//                    color = MaterialTheme.colors.background
//                )
//
//                Row(
//                    modifier = Modifier
//                        .padding(
//                            start = 14.dp,
//                            end = 14.dp
//                        )
//                        .height(54.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Save to Profile"
//                    )
//
//                    Spacer(Modifier.weight(1f))
//
//                    Image(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable {
//
//                            },
//                        painter = painterResource(),
//                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
//                        contentDescription = null
//                    )
//                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colors.background
                )

                val scope = rememberCoroutineScope()
                Row(
                    modifier = Modifier
                        .padding(
                            start = 14.dp,
                            end = 14.dp
                        )
                        .height(54.dp)
                        .clickable {
                            onSavePhoto(pagerState.currentPage)

                            scope.launch {
                                sheetState.hide()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Save to Photos"
                    )

                    Spacer(Modifier.weight(1f))

                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = Icons.Outlined.Check,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                        contentDescription = null
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colors.background
                )
            }
        },
        sheetState = sheetState
    ) {

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
                            ResourceImage(
                                resourceName = "user.png",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
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

                val moveHeadingPos = remember { mutableStateOf(false) }
                val topPadding = animateDpAsState(
                    targetValue = if (!moveHeadingPos.value) 154.dp else 100.dp,
                    animationSpec = tween(600)
                )

                val leftPadding = animateDpAsState(
                    targetValue = if (!moveHeadingPos.value) 24.dp else 0.dp,
                    animationSpec = tween(600)
                )

                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current

                Row(
                    modifier = Modifier
                        .padding(
                            top = topPadding.value
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    TextField(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(
                                start = 24.dp,
                                end = leftPadding.value,
                            )
                            .onFocusEvent {
                                if (it.hasFocus) {
                                    promptText = ""
                                }
                            }
                            .focusRequester(focusRequester),
                        enabled = state !is ViewState.Generated,
                        value = promptText,
                        textStyle = textStyle,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color(0xFFF06441),
                            disabledTextColor = Color(0xFFF06441),
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        onValueChange = {
                            promptText = it
                        }
                    )

                    AnimatedVisibility(
                        visible = moveHeadingPos.value
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    end = 18.dp
                                ),
                            onClick = {
                                moveHeadingPos.value = false

                                scope.launch {
                                    headingAnimation.animateTo(0f, tween(600))
                                }

                                onResetState()
                            }
                        ) {
                            ResourceImage(
                                modifier = Modifier
                                    .size(24.dp),
                                resourceName = "edit_icon.png"
                            )
                        }
                    }
                }

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
                                            animation = tween(
                                                400,
                                                easing = FastOutLinearInEasing
                                            ),
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

                                    ResourceImage(
                                        modifier = Modifier
                                            .padding(
                                                top = 25.dp,
                                                start = 31.dp
                                            )
                                            .offset(y = starAnimPos.dp)
                                            .size(114.dp),
                                        resourceName = "loading_star.png"
                                    )

                                    ResourceImage(
                                        modifier = Modifier
                                            .padding(
                                                top = 60.dp,
                                                start = 130.dp
                                            )
                                            .offset(y = circleAnimPos.dp)
                                            .size(84.dp),
                                        resourceName = "loading_circle.png"
                                    )

                                    ResourceImage(
                                        modifier = Modifier
                                            .padding(
                                                start = 200.dp
                                            )
                                            .offset(y = triangleAnimPos.dp)
                                            .size(100.dp),
                                        resourceName = "loading_triangle.png"
                                    )
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
                                    launch {
                                        headingAnimation.animateTo(1f, tween(600))
                                    }
                                    launch {
                                        moveHeadingPos.value = true
                                    }
                                }

                                HorizontalPager(
                                    modifier = Modifier
                                        .padding(
                                            top = 56.dp
                                        ),
                                    contentPadding = PaddingValues(
                                        start = 40.dp,
                                        end = 40.dp
                                    ),
                                    state = pagerState,
                                    verticalAlignment = Alignment.Top
                                ) { page ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        val generatedImg = targetState.images[page]

                                        Box(
                                            modifier = Modifier
                                                .size(271.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color.Red)
                                        ) {
                                            Image(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                bitmap = generatedImg.bitmap,
                                                contentDescription = null
                                            )
                                        }

                                        AnimatedVisibility(
                                            visible = pagerState.currentPageOffsetFraction < 0.1 && pagerState.settledPage == page,
                                            enter = fadeIn(
                                                animationSpec = tween(250)
                                            ),
                                            exit = fadeOut(
                                                animationSpec = tween(250)
                                            )
                                        ) {
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
                                                onClick = {
                                                    if (!generatedImg.isSaved) {
                                                        scope.launch {
                                                            sheetState.show()
                                                        }
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text =  if (generatedImg.isSaved) "Saved!" else "Save",
                                                    style = MaterialTheme.typography.h6
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}