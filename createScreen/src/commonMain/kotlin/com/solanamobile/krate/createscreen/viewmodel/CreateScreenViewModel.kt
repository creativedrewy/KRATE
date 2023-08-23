package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class ViewState() {

    data object Prompting : ViewState()

    data object Creating : ViewState()

    data class Generated(
        val bitmap: ImageBitmap? = null
    ): ViewState()
}

@OptIn(ExperimentalResourceApi::class)
@Provides
class CreateScreenViewModel(
    private val getImgRepository: GetImgRepository,
): StateScreenModel<ViewState>(ViewState.Prompting) {

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Creating
            }

            delay(3000)

            mutableState.update {
                ViewState.Generated()
            }
//            val imgString = getImgRepository.generateImage(prompt)
//            val decodedbytes = Base64.decode(imgString)
//
//            mutableState.update {
//                ViewState.Generated(
//                    bitmap = decodedbytes.toImageBitmap()
//                )
//            }
        }
    }
}