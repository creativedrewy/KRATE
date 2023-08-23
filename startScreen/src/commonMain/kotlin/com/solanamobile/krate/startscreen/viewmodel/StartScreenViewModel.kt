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
    val loaded: Boolean = true
)

@Provides
class StartScreenViewModel: StateScreenModel<StartScreenViewState>(StartScreenViewState()) {
    
}