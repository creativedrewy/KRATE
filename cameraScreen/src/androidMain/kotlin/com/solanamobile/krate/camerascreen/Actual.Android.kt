package com.solanamobile.krate.camerascreen

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.solanamobile.krate.extension.ui.navBarBottomPadding
import io.ktor.util.moveToByteArray
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
    maskImage: ImageBitmap,
    photoTaken: (ImageBitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

    val cameraSelector = remember {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
    }

    LaunchedEffect(Unit) {
        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
            ProcessCameraProvider.getInstance(context).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, executor)
            }
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    var (screenWidth, screenHeight) = remember { Pair(0, 0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                screenWidth = it.width
                screenHeight = it.height
            }
    ) {
        AndroidView(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
                .align(Alignment.Center)
                .fillMaxWidth(),
            factory = { previewView },
        )

        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(1f),
            bitmap = maskImage,
            contentDescription = null
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = 16.dp
                )
                .navBarBottomPadding()
                .size(84.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ),
            onClick = {
                imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val byteArray: ByteArray = image.planes[0].buffer.moveToByteArray()

                        val srcPhoto = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                        val portraitBmp = if (srcPhoto.width > srcPhoto.height) {
                            val rotMat = Matrix()
                            rotMat.setRotate(90f)

                            Bitmap.createBitmap(srcPhoto, 0, 0, srcPhoto.width, srcPhoto.height, rotMat, false)
                        } else {
                            srcPhoto
                        }

                        val (w, h) = portraitBmp.width to portraitBmp.height
                        val trimmedBmp = if (w > screenWidth) {
//                            Bitmap.createBitmap(portraitBmp, ((w.toDouble() - screenWidth.toDouble()) / 2).toInt(), 0, screenWidth, h)
                            portraitBmp
                        } else {
                            portraitBmp
                        }

                        val squareImg = Bitmap.createBitmap(trimmedBmp, 0, ((trimmedBmp.height.toDouble() - trimmedBmp.width.toDouble()) / 2).toInt(), trimmedBmp.width, trimmedBmp.width)
                        val imageBitmap = squareImg.asImageBitmap()

                        image.close()

                        photoTaken(imageBitmap)
                    }

                })
            }
        ) {
            Text(
                text = "START",
                style = MaterialTheme.typography.h6
            )
        }
    }
}