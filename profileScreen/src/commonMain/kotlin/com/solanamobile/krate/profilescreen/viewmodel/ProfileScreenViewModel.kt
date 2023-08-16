package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.update

data class SomeState(
    val myObj: String = "Hello"
)

@Provides
class ProfileScreenViewModel(): StateScreenModel<SomeState>(SomeState()) {

    fun modifyState() {
        mutableState.update {
            it.copy(
                myObj = "You clicked"
            )
        }
    }
}