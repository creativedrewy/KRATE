package com.solanamobile.krate.createscreen.repository

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides

@Provides
actual class MediaRepository(
    private val context: Context
) {
    actual fun saveBitmap(bmp: ImageBitmap) {
        Logger.v(tag = "ANDREW") { "You are doing something $context" }
    }
}