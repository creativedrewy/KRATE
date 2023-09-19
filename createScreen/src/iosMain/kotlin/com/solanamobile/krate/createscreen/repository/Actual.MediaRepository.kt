package com.solanamobile.krate.createscreen.repository

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import com.moriatsushi.koject.Provides
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIImage

@Provides
actual class MediaRepository {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun saveBitmap(bmp: ImageBitmap) {
        PHPhotoLibrary.requestAuthorization { status ->
            if (status == PHAuthorizationStatusAuthorized) {
                PHPhotoLibrary.sharedPhotoLibrary().performChanges({
                    val bytes = Image.makeFromBitmap(bmp.asSkiaBitmap()).encodeToData()!!.bytes
                    val nsData = bytes.toNSData()

                    val img = UIImage(data = nsData)
                    PHAssetChangeRequest.creationRequestForAssetFromImage(img)
                }, null)
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun ByteArray.toNSData(): NSData = usePinned {
        NSData.create(bytes = it.addressOf(0), this.size.convert())
    }

    actual fun imageBitmapToByteArray(bmp: ImageBitmap): ByteArray {
        TODO("Final implementation required")
    }
}