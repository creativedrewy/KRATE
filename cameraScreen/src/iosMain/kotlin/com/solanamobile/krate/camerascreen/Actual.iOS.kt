package com.solanamobile.krate.camerascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import com.solanamobile.krate.extension.ui.navBarBottomPadding
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDuoCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInUltraWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVVideoCodecKey
import platform.AVFoundation.AVVideoCodecTypeJPEG
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.fileDataRepresentation
import platform.AVFoundation.position
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreGraphics.CGRect
import platform.Foundation.NSError
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIImage
import platform.UIKit.UIView
import platform.darwin.NSObject

private sealed interface CameraAccess {
    object Undefined : CameraAccess
    object Denied : CameraAccess
    object Authorized : CameraAccess
}

actual class PermissionState(
    granted: Boolean = false,
    action: () -> Unit = { }
) {
    actual val allGranted: Boolean = granted

    actual val requestAction: () -> Unit = action
}

@Composable
actual fun getPermissionState(): PermissionState {
    var cameraAccess: CameraAccess by remember { mutableStateOf(CameraAccess.Undefined) }

    LaunchedEffect(Unit) {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                cameraAccess = CameraAccess.Authorized
            }

            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> {
                cameraAccess = CameraAccess.Denied
            }

            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(
                    mediaType = AVMediaTypeVideo
                ) { success ->
                    cameraAccess = if (success) CameraAccess.Authorized else CameraAccess.Denied
                }
            }
        }
    }

    return PermissionState(
        granted = cameraAccess is CameraAccess.Authorized,
        action = {
            AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        }
    )
}

@Composable
actual fun CameraPreview(
    maskImage: ImageBitmap,
    photoTaken: (ImageBitmap) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val camera: AVCaptureDevice? = remember {
            AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
                deviceTypes = listOf(
                    AVCaptureDeviceTypeBuiltInWideAngleCamera,
                    AVCaptureDeviceTypeBuiltInDualWideCamera,
                    AVCaptureDeviceTypeBuiltInDualCamera,
                    AVCaptureDeviceTypeBuiltInUltraWideCamera,
                    AVCaptureDeviceTypeBuiltInDuoCamera
                ),
                mediaType = AVMediaTypeVideo,
                position = AVCaptureDevicePositionFront,
            ).devices.firstOrNull() as? AVCaptureDevice
        }

        var img = remember { mutableStateOf<ImageBitmap?>(null) }

        if (img.value == null) {
            camera?.let {
                RealDeviceCamera(
                    camera = it,
                    photoTaken = {
                        img.value = it
                    }
                )
            }
        } else {
            img.value?.let {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center),
                    bitmap = it,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun BoxScope.RealDeviceCamera(
    camera: AVCaptureDevice,
    photoTaken: (ImageBitmap) -> Unit
) {
    val capturePhotoOutput = remember { AVCapturePhotoOutput() }
    var capturePhotoStarted by remember { mutableStateOf(false) }

    val captureSession: AVCaptureSession = remember {
        val captureSession = AVCaptureSession()
        captureSession.sessionPreset = AVCaptureSessionPresetPhoto

        val captureDeviceInput = AVCaptureDeviceInput.deviceInputWithDevice(device = camera, error = null)!!
        captureSession.addInput(captureDeviceInput)
        captureSession.addOutput(capturePhotoOutput)

        captureSession
    }

    val cameraPreviewLayer = remember { AVCaptureVideoPreviewLayer(session = captureSession) }

    val photoCaptureDelegate = remember {
        object : NSObject(), AVCapturePhotoCaptureDelegateProtocol {
            override fun captureOutput(
                output: AVCapturePhotoOutput,
                didFinishProcessingPhoto: AVCapturePhoto,
                error: NSError?
            ) {
                val photoData = didFinishProcessingPhoto.fileDataRepresentation()
                if (photoData != null) {

                    val uiImage = UIImage(photoData)
                    val croppedImage = cropToSquare(uiImage)

                    croppedImage.toSkiaImage()?.let {
                        photoTaken(it.toComposeImageBitmap())
                    }
                }

                capturePhotoStarted = false
            }
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            val cameraContainer = UIView()

            cameraContainer.layer.addSublayer(cameraPreviewLayer)
            cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            captureSession.startRunning()
            cameraContainer
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)

            view.layer.setFrame(rect)
            cameraPreviewLayer.setFrame(rect)
            CATransaction.commit()
        },
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
            capturePhotoStarted = true
            val photoSettings = AVCapturePhotoSettings.photoSettingsWithFormat(
                format = mapOf(AVVideoCodecKey to AVVideoCodecTypeJPEG)
            )

            if (camera.position == AVCaptureDevicePositionFront) {
                capturePhotoOutput.connectionWithMediaType(AVMediaTypeVideo)?.automaticallyAdjustsVideoMirroring = false
                capturePhotoOutput.connectionWithMediaType(AVMediaTypeVideo)?.videoMirrored = true
            }

            capturePhotoOutput.capturePhotoWithSettings(
                settings = photoSettings,
                delegate = photoCaptureDelegate
            )
        }
    ) {
        Text(
            text = "START",
            style = MaterialTheme.typography.h6
        )
    }
}
