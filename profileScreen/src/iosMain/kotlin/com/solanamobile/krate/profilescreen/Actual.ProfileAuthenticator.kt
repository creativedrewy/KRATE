package com.solanamobile.krate.profilescreen

import androidx.compose.runtime.Composable
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel

@Composable
actual fun configureScreen(vm: ProfileScreenViewModel) {
    TODO()
}

@Provides
actual class ProfileAuthenticator() {

    actual fun init() {
        TODO("Provide official implementation")
    }

    actual fun authenticate() {
        TODO("Provide official implementation")
    }

}