package com.solanamobile.krate.profilescreen

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import com.moriatsushi.koject.Provides
import com.solanamobile.krate.profilescreen.viewmodel.ProfileScreenViewModel

@Composable
actual fun configureScreen(vm: ProfileScreenViewModel) {
    Logger.v { "Setup an needed iOS dependencies here" }
}

@Provides
actual class ProfileAuthenticator() {

    actual fun init() {
        Logger.v { "Initialize authenticator if needed" }
    }

    actual fun authenticate() {
        Logger.v { "Authenticate user on iOS" }
    }

}