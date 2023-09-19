package com.solanamobile.krate.chooserscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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

    }
}