package com.solanamobile.krate.ui

import com.solanamobile.krate.Res
import com.solanamobile.krate.extension.compositionlocal.ResourceLocator
import io.github.skeptick.libres.images.Image

val resLocator = object : ResourceLocator {
    private val map = mapOf(
        "icon_caret_fill" to Res.image.icon_caret_fill,
        "icon_caret_line" to Res.image.icon_caret_line,
        "krate0" to Res.image.krate0,
        "krate1" to Res.image.krate1,
        "krate2" to Res.image.krate2,
        "krate3" to Res.image.krate3,
        "krate4" to Res.image.krate4,
    )

    override fun getResource(name: String): Image {
        return map[name]!!
    }
}