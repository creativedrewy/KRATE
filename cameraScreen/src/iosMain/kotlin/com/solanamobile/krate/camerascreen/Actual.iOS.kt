package com.solanamobile.krate.camerascreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

actual class PermissionState {
    actual val allGranted: Boolean = false

    actual val requestAction: () -> Unit = {
        TODO("Final implementation required")
    }
}

@Composable
actual fun getPermissionState(): PermissionState {
    TODO("Final implementation required")
}

@Composable
actual fun CameraPreview(
    maskImage: ImageBitmap,
    photoTaken: (ImageBitmap) -> Unit
) {
    TODO("Final implementation required")
}