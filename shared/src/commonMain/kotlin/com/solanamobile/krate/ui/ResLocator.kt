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
    private val map = mapOf(
        "icon_caret_fill" to Res.image.icon_caret_fill,
        "icon_caret_line" to Res.image.icon_caret_line,
        "krate0" to Res.image.krate0,
        "krate1" to Res.image.krate1,
        "krate2" to Res.image.krate2,
        "krate3" to Res.image.krate3,
        "krate4" to Res.image.krate4,
        "edit_icon" to Res.image.edit_icon,
        "loading_circle" to Res.image.loading_circle,
        "loading_star" to Res.image.loading_star,
        "loading_triangle" to Res.image.loading_triangle,
        "user" to Res.image.user,
        "wallet" to Res.image.wallet,
        "wallet2" to Res.image.wallet2,
        "wallet3" to Res.image.wallet3,
        "wallet4" to Res.image.wallet4,
    )

    override fun getResource(name: String): Image {
        return map[name]!!
    }

    override suspend fun getImageBitmap(name: String): ImageBitmap {
        return resource(name).readBytes().toImageBitmap()
    }
}