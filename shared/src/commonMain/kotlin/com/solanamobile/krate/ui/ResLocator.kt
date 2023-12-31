package com.solanamobile.krate.ui

import androidx.compose.ui.graphics.ImageBitmap
import com.solanamobile.krate.extension.compositionlocal.ResourceLocator
import com.solanamobile.krate.extension.graphics.toImageBitmap
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

    override suspend fun getResourceBytes(name: String): ByteArray {
        return resource(name).readBytes()
    }

}