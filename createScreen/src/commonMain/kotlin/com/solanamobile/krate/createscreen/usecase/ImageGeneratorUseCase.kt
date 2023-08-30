package com.solanamobile.krate.createscreen.usecase

import androidx.compose.ui.graphics.ImageBitmap
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import com.solanamobile.krate.extension.graphics.toImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Provides
class ImageGeneratorUseCase(
    private val getImgRepository: GetImgRepository
) {

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImages(prompt: String): List<ImageBitmap> {
        val bitmaps = (0..0).map {
            val imgString = getImgRepository.generateImage(prompt)

            val decodedbytes = Base64.decode(imgString)
            decodedbytes.toImageBitmap()
        }

        return bitmaps
    }

}