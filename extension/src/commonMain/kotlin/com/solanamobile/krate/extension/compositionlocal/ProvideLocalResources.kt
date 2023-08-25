package com.solanamobile.krate.extension.compositionlocal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.github.skeptick.libres.images.Image

interface ResourceLocator {
    fun getResource(name: String): Image
}

val LocalResourceLocator = compositionLocalOf<ResourceLocator> {
    object: ResourceLocator {
        override fun getResource(name: String): Image {
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
