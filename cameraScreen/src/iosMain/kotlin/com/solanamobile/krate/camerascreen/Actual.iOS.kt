package com.solanamobile.krate.camerascreen

import androidx.compose.runtime.Composable

actual class PermissionState {
    actual val allGranted: Boolean = false

    actual val requestAction: () -> Unit = { }
}

@Composable
actual fun getPermissionState(): PermissionState {
    TODO()
}

@Composable
actual fun CameraPreview() {
    TODO()
}