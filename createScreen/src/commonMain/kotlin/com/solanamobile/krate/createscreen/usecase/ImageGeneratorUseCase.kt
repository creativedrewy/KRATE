package com.solanamobile.krate.createscreen.usecase

import androidx.compose.ui.graphics.ImageBitmap
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import com.solanamobile.krate.extension.graphics.toImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

data class ImageData(
    val sourceBytes: String,
    val bitmap: ImageBitmap
)

@Provides
class ImageGeneratorUseCase(
    private val getImgRepository: GetImgRepository
) {

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun generateImages(prompt: String, imageCount: Int = 4): List<ImageData> {
        val bitmaps = (0..< imageCount).map {
            val imgString = getImgRepository.generateImage(prompt)

            val decodedbytes = Base64.decode(imgString)

            ImageData(
                sourceBytes = imgString,
                bitmap = decodedbytes.toImageBitmap()
            )
        }

        return bitmaps
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun inpaintImage(prompt: String, srcImg: ByteArray, maskImg: ByteArray): List<ImageData> {
        val bitmaps = (0..< 2).map {

            val source = Base64.encode(srcImg)
            val mask = Base64.encode(maskImg)

            val imgString = getImgRepository.inpaintImage(prompt, source, mask)

            val decodedbytes = Base64.decode(imgString)
            ImageData(
                sourceBytes = imgString,
                bitmap = decodedbytes.toImageBitmap()
            )
        }

        return bitmaps
    }
}