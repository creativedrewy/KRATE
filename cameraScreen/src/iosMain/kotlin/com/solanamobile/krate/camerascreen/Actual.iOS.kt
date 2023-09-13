package com.solanamobile.krate.camerascreen

import androidx.compose.runtime.Composable

actual class PermissionState {
    actual val isGranted: Boolean = false

    actual val requestAction: () -> Unit = { }
}

@Composable
actual fun getPermissions(): PermissionState {
    TODO()
}