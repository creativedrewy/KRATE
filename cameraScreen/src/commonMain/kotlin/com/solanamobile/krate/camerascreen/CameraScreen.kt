package com.solanamobile.krate.camerascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.solanamobile.krate.extension.ui.ResourceImage

class CameraScreen: Screen {

    @Composable
    override fun Content() {
        CameraScreenContent()
    }
}

@Composable
fun CameraScreenContent() {
    val permissionState = getPermissionState()

    var isPhotoTaken = remember { mutableStateOf(false) }
    var takenPhoto = remember { mutableStateOf<ImageBitmap?>(null) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        if (!permissionState.allGranted) {
            ResourceImage(
                modifier = Modifier
                    .padding(
                        top = 120.dp,
                    )
                    .width(85.dp)
                    .height(89.dp),
                resourceName = "camera_lines.png"
            )

            Column(
                modifier = Modifier
                    .padding(
                        top = 236.dp,
                        start = 24.dp
                    )
            ) {
                Text(
                    text = "KRATE\nNEEDS\nACCESS\nTO YOUR\n CAMERA",
                    color = Color(0xFF172C4A),
                    style = MaterialTheme.typography.h2.copy(
                        fontSize = 48.sp,
                        lineHeight = 48.sp
                    )
                )

                ResourceImage(
                    modifier = Modifier
                        .padding(
                            top = 31.dp
                        )
                        .size(96.dp)
                        .clickable {
                            permissionState.requestAction()
                        },
                    resourceName = "ok_lines.png"
                )
            }
        } else if (!isPhotoTaken.value) {
            CameraPreview(
                photoTaken = {
                    takenPhoto.value = it

                    isPhotoTaken.value = true
                }
            )
        } else {
            takenPhoto.value?.let {
                Image(
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.Gray),
                    contentScale = ContentScale.Fit,
                    bitmap = it,
                    contentDescription = null
                )
            }
        }
    }
}

expect class PermissionState {
    val allGranted: Boolean
    val requestAction: () -> Unit
}

@Composable
expect fun getPermissionState(): PermissionState

@Composable
expect fun CameraPreview(
    photoTaken: (ImageBitmap) -> Unit
)