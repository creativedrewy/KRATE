package com.solanamobile.krate.profilescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.extension.ui.ResourceImage
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel
import com.solanamobile.krate.profilescreen.viewmodel.ProfileViewState

object ProfileScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadMintedNfts()
        }

        ProfileScreenContent(
            state = state
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreenContent(
    state: ProfileViewState
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
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                title = { },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(
                                start = 16.dp
                            )
                            .clickable {
                                navigator.pop()
                            },
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(127.dp)
                        .background(MaterialTheme.colors.surface)
                )

                Column(
                    modifier = Modifier
                        .padding(
                            top = 64.dp
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResourceImage(
                        modifier = Modifier
                            .width(92.dp)
                            .height(84.dp),
                        resourceName = "profile_top.png"
                    )

                    Button(
                        modifier = Modifier
                            .padding(
                                top = 14.dp
                            ),
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            disabledBackgroundColor = MaterialTheme.colors.primary
                        ),
                        enabled = false
                    ) {
                        Text(
                            text = "Claim Profile",
                            color = Color(0xFF0D1F33),
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 8.dp
                            ),
                        text = "(Coming Soon)",
                        color = Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                }
            }

            val pagerState = rememberPagerState()

            ScrollableTabRow(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                backgroundColor = MaterialTheme.colors.background,
                selectedTabIndex = pagerState.currentPage,
                edgePadding = (-16).dp,
                divider = {
                    TabRowDefaults.Divider(
                        thickness = 4.dp,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            ) {
                Tab(
                    text = {
                        Text(
                            text = "Your Krate",
                            color = MaterialTheme.colors.onSurface,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    selected = true,
                    onClick = { },
                )
            }

            HorizontalPager(
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                pageCount = 1,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { _ ->
                Column(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.surface),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = 72.dp
                            )
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background)
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 26.dp,
                                start = 24.dp,
                                end = 24.dp
                            ),
                        text = "You don’t have any creations yet. Try creating something and then tapping save to get started.",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}