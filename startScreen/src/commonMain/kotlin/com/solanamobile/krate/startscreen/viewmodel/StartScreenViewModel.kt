package com.solanamobile.krate.startscreen.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class StartScreenViewState(
    val logos: List<ImageBitmap> = listOf()
)

@Provides
class StartScreenViewModel: StateScreenModel<StartScreenViewState>(StartScreenViewState()) {
    
    @OptIn(ExperimentalResourceApi::class)
    fun loadLogos() {
        coroutineScope.launch {
//            resource("krate-logo.png").readBytes().size
        }
    }
    
}