package com.solanamobile.krate.profilescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.solanamobile.krate.extension.getScreenModel
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel
import com.solanamobile.krate.profilescreen.viewmodel.ProfileViewState

object ProfileScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileScreenViewModel = getScreenModel()
        val state by viewModel.state.collectAsState()

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(238.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(171.dp)
                    .background(MaterialTheme.colors.surface)
            )

            Row(
                modifier = Modifier
                    .height(83.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 20.dp
                        )
                        .size(83.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                )

                Column(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            bottom = 10.dp
                        )
                ) {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )

                    Text(
                        text = "123Monkey",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
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
                        text = "Creations",
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
        ) { page ->
            when (page) {
                0 -> {
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
}