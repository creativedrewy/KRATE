package com.solanamobile.krate.createscreen.repository

import androidx.compose.ui.graphics.ImageBitmap

expect class MediaRepository {
    fun saveBitmap(bmp: ImageBitmap)
    fun imageBitmapToByteArray(bmp: ImageBitmap): ByteArray
}