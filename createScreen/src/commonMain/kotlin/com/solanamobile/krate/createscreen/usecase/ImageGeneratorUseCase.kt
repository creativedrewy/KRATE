package com.solanamobile.krate.createscreen.usecase

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import co.touchlab.kermit.Logger
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
    suspend fun inpaintImage(prompt: String, bmp1: ByteArray, bmp2: ByteArray): List<ImageData> {
        val bitmaps = (0..< 2).map {

            val encodedImg1 = Base64.encode(bmp1)
            val encodedImg2 = Base64.encode(bmp2)

            Logger.v { "Your encoded img: $encodedImg1" }

            val imgString = getImgRepository.inpaintImage(prompt, encodedImg1, encodedImg2)

            val decodedbytes = Base64.decode(imgString)
            ImageData(
                sourceBytes = imgString,
                bitmap = decodedbytes.toImageBitmap()
            )
        }

        return bitmaps
    }

    private fun imgBitmapToByteArray(bitmap: ImageBitmap): ByteArray {
        Logger.v { ":::: Your image dim: ${bitmap.width}, ${bitmap.height}" }


        val pixelData = IntArray(bitmap.width * bitmap.height)
        bitmap.readPixels(pixelData)

        val resultByteArray = ByteArray(bitmap.width * bitmap.height * 4)
        pixelData.forEachIndexed() { i, colorInt ->
            val color = Color(colorInt)

            Logger.v { ":::: Color RGB ${color.red}, ${color.green}, ${color.blue}" }

            resultByteArray[i * 4 + 0] = (color.red * 255).toInt().toByte()
            resultByteArray[i * 4 + 1] = (color.green * 255).toInt().toByte()
            resultByteArray[i * 4 + 2] = (color.blue * 255).toInt().toByte()
            resultByteArray[i * 4 + 3] = (255).toByte()
        }

        return resultByteArray
    }
}