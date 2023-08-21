package com.solanamobile.krate.createscreen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.createscreen.repository.GetImgRepository
import com.solanamobile.krate.extension.graphics.toImageBitmap
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
fun <T> List<T>.isReady(block: @Composable () -> T) {
    if (this.isNotEmpty()) {
        block()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Provides
class CreateScreenViewModel(
    private val getImgRepository: GetImgRepository,
): StateScreenModel<ViewState>(ViewState.Creating) {

    private val _resources: MutableStateFlow<List<ImageBitmap>> = MutableStateFlow(listOf())

    val resources = _resources.asStateFlow()

    fun loadResources() {
        coroutineScope.launch {
            _resources.update {
                listOf(
                    resource("user.png").readBytes().toImageBitmap(),
                    resource("loading_star.png").readBytes().toImageBitmap(),
                    resource("loading_circle.png").readBytes().toImageBitmap(),
                    resource("loading_triangle.png").readBytes().toImageBitmap()
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