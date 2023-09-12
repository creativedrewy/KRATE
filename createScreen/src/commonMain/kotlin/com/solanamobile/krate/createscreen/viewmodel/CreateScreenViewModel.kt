package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.MediaRepository
import com.solanamobile.krate.createscreen.usecase.ImageGeneratorUseCase
import com.solanamobile.krate.extensions.ApiKeys
import com.solanamobile.krate.localstorage.UserAccountUseCase
import com.underdogprotocol.api.CreateNftRequest
import com.underdogprotocol.api.UnderdogApiV2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi

data class GeneratedImg(
    val imgSrc: String,
    val bitmap: ImageBitmap,
    val isSavedToProfile: Boolean = false
)

sealed class ViewState() {
    data object Prompting : ViewState()
    data object Creating : ViewState()

    data class Generated(
        val images: List<GeneratedImg> = listOf()
    ): ViewState()
}

sealed class SavingState() {
    data object Resting: SavingState()
    data object Saving: SavingState()
    data object Saved: SavingState()
}

@OptIn(ExperimentalResourceApi::class)
@Provides
class CreateScreenViewModel(
    private val imgGeneratorUseCase: ImageGeneratorUseCase,
    private val mediaRepository: MediaRepository,
    private val acctUseCase: UserAccountUseCase
): StateScreenModel<ViewState>(ViewState.Prompting) {

    private val _savingState = MutableStateFlow<SavingState>(SavingState.Resting)

    val savingState = _savingState.asStateFlow()

    fun resetState() {
        mutableState.update {
            ViewState.Prompting
        }
    }

    fun finishSave() {
        _savingState.update { SavingState.Resting }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Creating
            }

            val generatedImgs = imgGeneratorUseCase.generateImages(prompt).map {
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

    fun inpaintImageFromPrompt(prompt: String, sourceImage: ByteArray, maskImage: ByteArray) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Creating
            }

            val generatedImgs = imgGeneratorUseCase.inpaintImage(prompt, sourceImage, maskImage).map {
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
            _savingState.update { SavingState.Saving }

            val selectedImage = (mutableState.value as ViewState.Generated).images[index]

            val request = CreateNftRequest(
                name = "KRATE Creation",
                image = selectedImage.imgSrc,
                receiverAddress = acctUseCase.userAddress
            )

            val api = UnderdogApiV2(true)
            api.mintNft(request, 1, ApiKeys.NFT_API_KEY)

            delay(3000)

            _savingState.update { SavingState.Saved }

            mutableState.update {
                val state = (it as ViewState.Generated)

                state.copy(
                    images = state.images.mapIndexed { i, item ->
                        GeneratedImg(
                            imgSrc = item.imgSrc,
                            bitmap = item.bitmap,
                            isSavedToProfile = i == index
                        )
                    }
                )
            }
        }
    }

    fun saveToPhotos(index: Int) {
        coroutineScope.launch {
            _savingState.update { SavingState.Saving }

            val selectedImage = (mutableState.value as ViewState.Generated).images[index]

            mediaRepository.saveBitmap(selectedImage.bitmap)

            mutableState.update {
                (it as ViewState.Generated)
            }

            _savingState.update { SavingState.Saved }
        }
    }
}