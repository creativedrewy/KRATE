package com.solanamobile.krate.startscreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import com.moriatsushi.koject.Provides

data class StartScreenViewState(
    val loaded: Boolean = true
)

@Provides
class StartScreenViewModel: StateScreenModel<StartScreenViewState>(StartScreenViewState()) {

}