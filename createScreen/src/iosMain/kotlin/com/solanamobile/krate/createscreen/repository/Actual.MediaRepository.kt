package com.solanamobile.krate.createscreen.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.moriatsushi.koject.Provides
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHPhotoLibrary

@Provides
actual class MediaRepository {
    actual fun saveBitmap(bmp: ImageBitmap) {
        PHPhotoLibrary.requestAuthorization { status ->
            if (status == PHAuthorizationStatusAuthorized) {

            }
        }
    }

    //PHPhotoLibrary.requestAuthorization { status in
    //        if status == .authorized {
    //            PHPhotoLibrary.shared().performChanges {
    //                let request = PHAssetChangeRequest.creationRequestForAsset(from: image)
    //                request.creationDate = Date()
    //            } completionHandler: { success, error in
    //                if success {
    //                    print("Image saved to Photos directory successfully.")
    //                } else if let error = error {
    //                    print("Error saving image: \(error.localizedDescription)")
    //                }
    //            }
    //        } else if status == .denied {
    //            print("Permission to access Photos denied.")
    //        } else if status == .notDetermined {
    //            print("Permission to access Photos not determined.")
    //        } else if status == .restricted {
    //            print("Access to Photos is restricted.")
    //        }
    //    }
}