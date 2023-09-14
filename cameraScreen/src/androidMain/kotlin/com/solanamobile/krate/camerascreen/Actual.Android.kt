package com.solanamobile.krate.camerascreen

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.solanamobile.krate.extension.graphics.toImageBitmap
import io.ktor.util.moveToByteArray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PermissionState(
    granted: Boolean,
    action: () -> Unit
) {
    actual val allGranted: Boolean = granted

    actual val requestAction: () -> Unit = action
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun getPermissionState(): PermissionState {
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

private val executor = Executors.newSingleThreadExecutor()

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun CameraPreview(

) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewScope = rememberCoroutineScope()

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }

    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    var isFrontCamera by rememberSaveable { mutableStateOf(true) }

    val cameraSelector = remember(isFrontCamera) {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
    }

    LaunchedEffect(isFrontCamera) {
        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            ProcessCameraProvider.getInstance(context).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, executor)
            }
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    var capturePhotoStarted by remember { mutableStateOf(false) }

    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView },
        )

        CircularButton(
            imageVector = Icons.Default.Phone,
            modifier = Modifier.align(Alignment.BottomCenter).padding(36.dp),
            enabled = !capturePhotoStarted,
        ) {
            capturePhotoStarted = true

            imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val byteArray: ByteArray = image.planes[0].buffer.moveToByteArray()

                    val imageBitmap = byteArray.toImageBitmap()
                    image.close()
                }

            })

            viewScope.launch {
                // TODO: There is a known issue with Android emulator
                //  https://partnerissuetracker.corp.google.com/issues/161034252
                //  After 5 seconds delay, let's assume that the bug appears and publish a prepared photo
                delay(5000)

                if (capturePhotoStarted) {
//                    addLocationInfoAndReturnResult(
//                        resource("android-emulator-photo.jpg").readBytes().toImageBitmap()
//                    )
                }
            }
        }

        if (capturePhotoStarted) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp).align(Alignment.Center),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 8.dp,
            )
        }
    }
}

@Composable
fun CircularButton(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .run {
                if (enabled) {
                    clickable { onClick() }
                } else this
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun CircularButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    CircularButton(
        modifier = modifier,
        content = {
            Icon(imageVector, null, Modifier.size(34.dp), Color.White)
        },
        enabled = enabled,
        onClick = onClick
    )
}