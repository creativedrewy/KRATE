package com.solanamobile.krate.createscreen.usecase

import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository

@Provides
class ImageGeneratorUseCase(
    private val getImgRepository: GetImgRepository
) {

    suspend fun generateImages(prompt: String) {
//            val imgString = getImgRepository.generateImage(prompt)
//            val decodedbytes = Base64.decode(imgString)
//        bitmap = decodedbytes.toImageBitmap()
    }

}