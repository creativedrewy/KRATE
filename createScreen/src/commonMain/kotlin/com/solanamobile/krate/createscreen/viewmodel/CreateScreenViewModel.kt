package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import com.solanamobile.krate.extension.graphics.toImageBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class ViewState() {

    data object Prompting : ViewState()

    data object Creating : ViewState()

    data class Generated(
        val bitmap: ImageBitmap? = null
    ): ViewState()
}

@Composable
fun <T, U> Map<T, U>.isReady(block: @Composable () -> Unit) {
    if (this.isNotEmpty()) {
        block()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Provides
class CreateScreenViewModel(
    private val getImgRepository: GetImgRepository,
): StateScreenModel<ViewState>(ViewState.Prompting) {

    private val _resources: MutableStateFlow<Map<String, ImageBitmap>> = MutableStateFlow(mapOf())

    val resources = _resources.asStateFlow()

    fun loadResources() {
        coroutineScope.launch {
            _resources.update {
                mapOf(
                    "user" to resource("user.png").readBytes().toImageBitmap(),
                    "star" to resource("loading_star.png").readBytes().toImageBitmap(),
                    "circle" to resource("loading_circle.png").readBytes().toImageBitmap(),
                    "triangle" to resource("loading_triangle.png").readBytes().toImageBitmap(),
                    "wallet1" to resource("wallet.png").readBytes().toImageBitmap(),
                    "wallet2" to resource("wallet2.png").readBytes().toImageBitmap(),
                    "wallet3" to resource("wallet3.png").readBytes().toImageBitmap(),
                    "wallet4" to resource("wallet4.png").readBytes().toImageBitmap()
                )
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            mutableState.update {
                ViewState.Creating
            }

            delay(500)

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