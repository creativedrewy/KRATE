package com.solanamobile.krate.startscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.extension.graphics.toImageBitmap
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

data class StartScreenViewState(
    val logos: List<ImageBitmap> = listOf()
)

@Provides
class StartScreenViewModel: StateScreenModel<StartScreenViewState>(StartScreenViewState()) {
    
    @OptIn(ExperimentalResourceApi::class)
    fun loadLogos() {
        coroutineScope.launch {
            mutableState.update {
                it.copy(
                    logos = listOf(
                        resource("KRATE-0.png").readBytes().toImageBitmap(),
                        resource("KRATE-1.png").readBytes().toImageBitmap(),
                        resource("KRATE-2.png").readBytes().toImageBitmap(),
                        resource("KRATE-3.png").readBytes().toImageBitmap(),
                        resource("KRATE-4.png").readBytes().toImageBitmap()
                    )
                )
            }
        }
    }
    
}