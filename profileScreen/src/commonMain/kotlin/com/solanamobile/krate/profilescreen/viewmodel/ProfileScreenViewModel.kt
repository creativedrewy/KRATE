package com.solanamobile.krate.profilescreen.viewmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import com.moriatsushi.koject.Provides

data class ProfileViewState(
    val name: String = "Name"
)

@Provides
class ProfileScreenViewModel: StateScreenModel<ProfileViewState>(ProfileViewState()) {

}