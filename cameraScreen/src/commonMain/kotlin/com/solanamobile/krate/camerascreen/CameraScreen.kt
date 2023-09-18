package com.solanamobile.krate.camerascreen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.solanamobile.krate.extension.NavScreenProvider
import com.solanamobile.krate.extension.compositionlocal.LocalResourceLocator
import com.solanamobile.krate.extension.ui.ResourceImage
import kotlinx.coroutines.launch

class CameraScreen: Screen {

    @Composable
    override fun Content() {
        CameraScreenContent()
    }
}

@Composable
fun CameraScreenContent() {
    val permissionState = getPermissionState()

    val scope = rememberCoroutineScope()
    val resources = LocalResourceLocator.current
    val navigator = LocalNavigator.currentOrThrow

    val mask = remember { mutableStateOf<ImageBitmap?>(null) }
    scope.launch {
        mask.value = resources.getImageBitmap("photo_overlay.png")
    }

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
        } else {
            mask.value?.let { maskImage ->
                CameraPreview(
                    maskImage = maskImage,
                    photoTaken = { img ->
                        val screenProps = NavScreenProvider.CreateScreen(NavScreenProvider.CreateMode.Teleport, img)

                        navigator.push(ScreenRegistry.get(screenProps))
                    }
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
    maskImage: ImageBitmap,
    photoTaken: (ImageBitmap) -> Unit
)