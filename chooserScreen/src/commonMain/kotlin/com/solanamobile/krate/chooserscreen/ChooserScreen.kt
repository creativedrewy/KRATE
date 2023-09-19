package com.solanamobile.krate.chooserscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.ui.ResourceImage
import com.solanamobile.krate.extension.ui.statusBarTopPadding

class ChooserScreen: Screen {

    @Composable
    override fun Content() {
        ChooserScreenContent()
    }

}

@Composable
fun ChooserScreenContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .statusBarTopPadding(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ResourceImage(
                            modifier = Modifier
                                .width(138.dp)
                                .height(32.dp),
                            resourceName = "krate4.png"
                        )
                    }
                },
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
                .padding(
                    top = 40.dp,
                    start = 24.dp
                )
        ) {
            Text(
                text = "Teleport",
                style = MaterialTheme.typography.h6
            )

            Card(
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    )
                    .width(320.dp)
                    .height(205.dp)
                    .clickable {
                        navigator.push(ScreenRegistry.get(NavScreenProvider.CameraScreen))
                    },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                elevation = 8.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(
                                top = 40.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ResourceImage(
                            modifier = Modifier
                                .width(83.dp)
                                .height(72.dp),
                            resourceName = "teleport_left.png"
                        )

                        Image(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp
                                )
                                .size(24.dp),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )

                        ResourceImage(
                            modifier = Modifier
                                .width(77.dp)
                                .height(65.dp),
                            resourceName = "teleport_right.png"
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 24.dp
                            )
                            .fillMaxWidth(),
                        text = "Take a selfie and teleport yourself somewhere completely new.",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                modifier = Modifier
                    .padding(
                        top = 24.dp
                    ),
                text = "Text to Image",
                style = MaterialTheme.typography.h6
            )

            Card(
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    )
                    .width(320.dp)
                    .height(205.dp)
                    .clickable {
                        navigator.push(ScreenRegistry.get(NavScreenProvider.CreateScreen(
                            createMode = NavScreenProvider.CreateMode.Text2Img
                        )))
                    },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
                elevation = 8.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(
                                top = 20.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ResourceImage(
                            modifier = Modifier
                                .width(78.dp)
                                .height(91.dp),
                            resourceName = "text_left.png"
                        )

                        Image(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp
                                )
                                .size(24.dp),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )

                        ResourceImage(
                            modifier = Modifier
                                .width(77.dp)
                                .height(65.dp),
                            resourceName = "text_right.png"
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 24.dp,
                                start = 8.dp,
                                end = 8.dp
                            )
                            .fillMaxWidth(),
                        text = "Type anything you like. We'll make an image for you.",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}