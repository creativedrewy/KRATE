package com.solanamobile.krate.extension.compositionlocal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image

interface ResourceLocator {
    suspend fun getImageBitmap(name: String): ImageBitmap
}

val LocalResourceLocator = compositionLocalOf<ResourceLocator> {
    object: ResourceLocator {
        override suspend fun getImageBitmap(name: String): ImageBitmap {
            throw NotImplementedError("Please provide ResourceLocator implementation")
        }
    }
}

@Composable
fun ProvideResourceLocator(
    locator: ResourceLocator,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalResourceLocator provides locator) {
        content()
    }
}
