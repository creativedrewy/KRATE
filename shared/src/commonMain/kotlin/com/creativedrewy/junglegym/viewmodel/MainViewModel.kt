package com.creativedrewy.junglegym.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import com.creativedrewy.junglegym.coroutine.Dispatcher
import com.creativedrewy.junglegym.coroutine.Dispatchers
import com.creativedrewy.junglegym.graphics.toImageBitmap
import com.creativedrewy.junglegym.repository.GetImgRepository
import com.creativedrewy.junglegym.repository.PlatformRepository
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed class ViewState() {
    data object Default : ViewState()
    data object Loading : ViewState()
    data class Generated(
        val bitmap: ImageBitmap
    ): ViewState()
}

@Provides
class MainViewModel(
    private val repository: PlatformRepository,
    private val getImgRepository: GetImgRepository,
    @Dispatcher(Dispatchers.Main)
    private val dispatcher: CoroutineDispatcher
) {
    private val job = SupervisorJob()

    private val coroutineScope: CoroutineScope
        get() = CoroutineScope(job + dispatcher)

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Default)

    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt(prompt: String) {
        coroutineScope.launch {
            _viewState.update {
                ViewState.Loading
            }

            val imgString = getImgRepository.generateImage(prompt)

            val decodedbytes = Base64.decode(imgString)

            _viewState.update {
                ViewState.Generated(
                    bitmap = decodedbytes.toImageBitmap()
                )
            }
        }
    }
}