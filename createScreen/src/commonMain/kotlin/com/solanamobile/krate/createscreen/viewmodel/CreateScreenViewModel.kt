package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.graphics.toImageBitmap
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class ViewState() {

    data object Default : ViewState()

    data object Loading : ViewState()

    data class Generated(
        val bitmap: ImageBitmap? = null
    ): ViewState()
}

@Provides
class CreateScreenViewModel(
    private val getImgRepository: GetImgRepository,
): StateScreenModel<ViewState>(ViewState.Default) {

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Loading
            }

            val imgString = getImgRepository.generateImage(prompt)
            val decodedbytes = Base64.decode(imgString)

            mutableState.update {
                ViewState.Generated(
                    bitmap = decodedbytes.toImageBitmap()
                )
            }
        }
    }
}