package com.solanamobile.krate.extension.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import com.solanamobile.krate.extension.compositionlocal.LocalResourceLocator
import kotlinx.coroutines.launch

@Composable
fun ResourceImage(
    modifier: Modifier = Modifier,
    contentDesc: String? = null,
    colorFilter: ColorFilter? = null,
    resourceName: String,
) {
    val resLocator = LocalResourceLocator.current

    val scope = rememberCoroutineScope()
    val bmp = remember { mutableStateOf<ImageBitmap?>(null) }

    scope.launch {
        bmp.value = resLocator.getImageBitmap(resourceName)
    }

    bmp.value?.let {
        Image(
            modifier = modifier,
            bitmap = it,
            contentDescription = contentDesc,
            colorFilter = colorFilter
        )
    }
}