package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.ApiKeys
import com.solanamobile.krate.createscreen.repository.MediaRepository
import com.solanamobile.krate.createscreen.usecase.ImageGeneratorUseCase
import com.underdogprotocol.api.CreateNftRequest
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi

data class GeneratedImg(
    val imgSrc: String,
    val bitmap: ImageBitmap,
    val isSaved: Boolean = false
)

sealed class ViewState() {

    data object Prompting : ViewState()

    data object Creating : ViewState()

    data class Generated(
        val images: List<GeneratedImg> = listOf()
    ): ViewState()
}

@OptIn(ExperimentalResourceApi::class)
@Provides
class CreateScreenViewModel(
    private val imgGeneratorUseCase: ImageGeneratorUseCase,
    private val mediaRepository: MediaRepository
): StateScreenModel<ViewState>(ViewState.Prompting) {

    fun resetState() {
        mutableState.update {
            ViewState.Prompting
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Creating
            }

            val generatedImgs = imgGeneratorUseCase.generateImages(prompt, 1).map {
                GeneratedImg(
                    imgSrc = it.sourceBytes,
                    bitmap = it.bitmap,
                )
            }

            mutableState.update {
                ViewState.Generated(generatedImgs)
            }
        }
    }

    fun saveToProfile(index: Int) {
        coroutineScope.launch {
            val selectedImage = (mutableState.value as ViewState.Generated).images[index]

            val request = CreateNftRequest(
                name = "KRATE Creation",
                image = selectedImage.imgSrc,
                receiverAddress = "i5Ww8XokvATpEL8xmu8uXQhjSQMGzgHeB9N8VSDzX3p"
            )

            val api = UnderdogApiV2(true)
            api.mintNft(request, 1, ApiKeys.NFT_API_KEY)

            mutableState.update {
                val state = (it as ViewState.Generated)

                state.copy(
                    images = state.images.mapIndexed { i, item ->
                        GeneratedImg(
                            imgSrc = item.imgSrc,
                            bitmap = item.bitmap,
                            isSaved = i == index
                        )
                    }
                )
            }
        }
    }

    fun saveToPhotos(index: Int) {
        coroutineScope.launch {
            val selectedImage = (mutableState.value as ViewState.Generated).images[index]

            mediaRepository.saveBitmap(selectedImage.bitmap)

            mutableState.update {
                val state = (it as ViewState.Generated)

                it
            }
        }
    }
}