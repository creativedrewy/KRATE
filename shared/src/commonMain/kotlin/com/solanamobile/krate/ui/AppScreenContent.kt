package com.solanamobile.krate.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.solanamobile.krate.createscreen.CreateScreen
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.profilescreen.ProfileScreen
import com.solanamobile.krate.resources.font
import com.solanamobile.krate.startscreen.StartScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScreenContent() {
    ScreenRegistry {
        register<NavScreenProvider.StartScreen> {
            StartScreen()
        }
        register<NavScreenProvider.CreateScreen> {
            CreateScreen()
        }
        register<NavScreenProvider.ProfileScreen> {
            ProfileScreen
        }
    }

    val geographNormal = FontFamily(
        font(
            name = "GeographBlack",
            res = "testgeograph_black",
            weight = FontWeight.Black,
            style = FontStyle.Normal
        ),
        font(
            name = "GeographMedium",
            res = "testgeograph_medium",
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        )
    )

    val typography = Typography(
        h2 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Black,
            fontSize = 54.sp,
            lineHeight = 60.sp
        ),
        h3 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Black,
            fontSize = 32.sp,
            lineHeight = 60.sp
        )
    )

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Color(0xFFF0E7E0),
            surface = Color(0xFFF0DCD4),
            primary = Color(0xFFF07C6C),
        ),
        typography = typography
    ) {
        Navigator(
            screen = ScreenRegistry.get(NavScreenProvider.StartScreen)
        ) {
            SlideTransition(it)
        }
    }
}