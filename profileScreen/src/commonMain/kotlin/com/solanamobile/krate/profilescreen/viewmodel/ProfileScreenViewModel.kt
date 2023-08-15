package com.solanamobile.krate.profilescreen.viewmodel

import com.moriatsushi.koject.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SomeState(
    val myObj: String = "Hello"
)

@Provides
class ProfileScreenViewModel() {

    private val _viewState: MutableStateFlow<SomeState> = MutableStateFlow(SomeState())

    val viewState = _viewState.asStateFlow()

    fun modifyState() {
        _viewState.update {
            it.copy(
                myObj = "You clicked"
            )
        }
    }
}