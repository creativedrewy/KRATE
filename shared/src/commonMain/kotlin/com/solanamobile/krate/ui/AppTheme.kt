package com.solanamobile.krate.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.solanamobile.krate.resources.font

@Composable
fun KrateAppTheme(
    content: @Composable () -> Unit
) {
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
        defaultFontFamily = geographNormal,
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
        ),
        h4 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        ),
        h5 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        ),
        h6 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        subtitle1 = TextStyle(
            fontFamily = geographNormal,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Color(0xFFF0E7E0),
            surface = Color(0xFFF0DCD4),
            primary = Color(0xFFF07C6C),
            onSurface = Color(0xFF172C4A)
        ),
        typography = typography
    ) {
        content()
    }
}