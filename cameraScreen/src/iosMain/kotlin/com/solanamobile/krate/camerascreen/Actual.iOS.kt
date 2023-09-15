package com.solanamobile.krate.camerascreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

actual class PermissionState {
    actual val allGranted: Boolean = false

    actual val requestAction: () -> Unit = { }
}

@Composable
actual fun getPermissionState(): PermissionState {
    TODO()
}

@Composable
actual fun CameraPreview(
    photoTaken: (ImageBitmap) -> Unit
) {
    TODO()
}