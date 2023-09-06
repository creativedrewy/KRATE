package com.solanamobile.krate.startscreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.localstorage.EncryptedPrefsRepository

data class StartScreenViewState(
    val loaded: Boolean = true
)

@Provides
class StartScreenViewModel(
    private val prefsRepository: EncryptedPrefsRepository
): StateScreenModel<StartScreenViewState>(StartScreenViewState()) {

    init {
        prefsRepository.savePref("hi", "hello")
    }

}