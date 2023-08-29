package com.solanamobile.krate.startscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.solanamobile.krate.extension.compositionlocal.LocalResourceLocator
import com.solanamobile.krate.extension.ui.ResourceImage
import io.github.skeptick.libres.compose.painterResource

@Composable
fun AnimatedArrows() {
    Box {
        ResourceImage(
            modifier = Modifier
                .height(30.dp)
                .width(55.dp),
            resourceName = "icon_caret_line.png"
        )

        ResourceImage(
            modifier = Modifier
                .padding(
                    start = 8.dp
                )
                .height(30.dp)
                .width(55.dp),
            resourceName = "icon_caret_line.png"
        )

        ResourceImage(
            modifier = Modifier
                .padding(
                    start = 16.dp
                )
                .height(30.dp)
                .width(55.dp),
            resourceName = "icon_caret_line.png"
        )

        ResourceImage(
            modifier = Modifier
                .padding(
                    start = 24.dp
                )
                .height(30.dp)
                .width(55.dp),
            resourceName = "icon_caret_line.png"
        )

        ResourceImage(
            modifier = Modifier
                .padding(
                    start = 32.dp
                )
                .height(30.dp)
                .width(55.dp),
            resourceName = "icon_caret_fill.png"
        )
    }
}