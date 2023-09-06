package com.solanamobile.krate.startscreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.localstorage.UserAccountUseCase
import kotlinx.coroutines.launch

data class StartScreenViewState(
    val loaded: Boolean = true
)

@Provides
class StartScreenViewModel(
    private val accountUseCase: UserAccountUseCase
): StateScreenModel<StartScreenViewState>(StartScreenViewState()) {

    fun setupAccount() {
        coroutineScope.launch {
            if (!accountUseCase.accountExists()) {
                accountUseCase.createUserAccount()
            }
        }
    }

}