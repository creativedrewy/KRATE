package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.MediaRepository
import com.solanamobile.krate.createscreen.usecase.ImageGeneratorUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class ViewState() {

    data object Prompting : ViewState()

    data object Creating : ViewState()

    data class Generated(
        val images: List<ImageBitmap> = listOf()
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

            val generatedImgs = imgGeneratorUseCase.generateImages(prompt)

            mutableState.update {
                ViewState.Generated(generatedImgs)
            }
        }
    }

    fun saveToPhotos(index: Int) {
        coroutineScope.launch {
            val selectedImage = (mutableState.value as ViewState.Generated).images[index]

            Logger.v { "Your id: $selectedImage" }

            mediaRepository.saveBitmap(selectedImage)
        }
    }
}