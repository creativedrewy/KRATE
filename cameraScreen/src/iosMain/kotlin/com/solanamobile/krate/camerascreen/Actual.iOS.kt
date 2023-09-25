package com.solanamobile.krate.camerascreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

actual class PermissionState(
    granted: Boolean = false,
    action: () -> Unit = { }
) {
    actual val allGranted: Boolean = granted

    actual val requestAction: () -> Unit = action
}

@Composable
actual fun getPermissionState(): PermissionState {
    return PermissionState()
}

@Composable
actual fun CameraPreview(
    maskImage: ImageBitmap,
    photoTaken: (ImageBitmap) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

    }
}