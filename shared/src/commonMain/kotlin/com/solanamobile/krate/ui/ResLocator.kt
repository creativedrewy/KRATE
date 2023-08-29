package com.solanamobile.krate.ui

import androidx.compose.ui.graphics.ImageBitmap
import com.solanamobile.krate.Res
import com.solanamobile.krate.extension.compositionlocal.ResourceLocator
import com.solanamobile.krate.extension.graphics.toImageBitmap
import io.github.skeptick.libres.images.Image
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class)
val resLocator = object : ResourceLocator {
    private val inMemCache = mutableMapOf<String, ImageBitmap>()

    override suspend fun getImageBitmap(name: String): ImageBitmap {
        return if (inMemCache.containsKey(name)) {
            inMemCache[name]!!
        } else {
            val bmp = resource(name).readBytes().toImageBitmap()

            inMemCache[name] = bmp
            bmp
        }
    }
}