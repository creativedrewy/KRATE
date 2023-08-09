package com.creativedrewy.junglegym.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import com.creativedrewy.junglegym.coroutine.Dispatcher
import com.creativedrewy.junglegym.coroutine.Dispatchers
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

data class ViewState(
    val osString: String,
    val bitmap: ImageBitmap? = null
)

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

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState("OS will show here"))

    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    fun doSomething() {
        coroutineScope.launch {
            _viewState.update {
                it.copy(
                    osString = "This came from ViewModel: ${repository.getPlatformString()}"
                )
            }

            generateImageFromPrompt()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun generateImageFromPrompt() {
        coroutineScope.launch {
            val imgString = getImgRepository.generateImage("A cat")

            val decodedbytes = Base64.decode(imgString)

//            val test = ImageBitmap(512, 512)
//            test.readPixels(byteArrayToIntArray(decodedbytes))
//
//            _viewState.update {
//                it.copy(
//                    bitmap = test
//                )
//            }
        }
    }
}