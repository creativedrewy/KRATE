package com.solanamobile.krate.camerascreen

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

actual class PermissionState(
    granted: Boolean,
    action: () -> Unit
) {
    actual val isGranted: Boolean = granted

    actual val requestAction: () -> Unit = action
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun getPermissions(): PermissionState {
    val cameraPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA
        )
    )

    return PermissionState(
        granted = cameraPermissionState.allPermissionsGranted,
        action = {
            cameraPermissionState.launchMultiplePermissionRequest()
        }
    )
}