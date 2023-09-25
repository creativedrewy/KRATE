package com.solanamobile.krate.camerascreen

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateCopyWithColorSpace
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIImage

//import UIKit
//
//// Assuming 'image' is a CVPixelBuffer
//let byteArray = Array(UnsafeBufferPointer(start: CVPixelBufferGetBaseAddress(image), count: CVPixelBufferGetDataSize(image)))
//let srcPhoto = UIImage(data: Data(byteArray))
//
//if let srcPhoto = srcPhoto {
//    let portraitBmp: UIImage
//    if srcPhoto.size.width > srcPhoto.size.height {
//        let downScaleFactor = screenHeight / srcPhoto.size.width // Need to use width as photo is un-rotated
//
//        let rotMat = CGAffineTransform(rotationAngle: .pi / 2)
//        let rot = srcPhoto.transformed(by: rotMat)
//        portraitBmp = rot.scaleBy(x: downScaleFactor, y: downScaleFactor)
//    } else {
//        fatalError("App does not currently handle cameras with hardware configured as portrait")
//    }
//
//    let w = Int(portraitBmp.size.width)
//    let h = Int(portraitBmp.size.height)
//    let trimmedBmp: UIImage
//    if w > screenWidth {
//        trimmedBmp = portraitBmp.crop(rect: CGRect(x: (CGFloat(w) - screenWidth) / 2, y: 0, width: CGFloat(screenWidth), height: CGFloat(h)))!
//    } else {
//        trimmedBmp = portraitBmp
//    }
//
//    let squareImg = trimmedBmp.crop(rect: CGRect(x: 0, y: (CGFloat(trimmedBmp.size.height) - CGFloat(trimmedBmp.size.width)) / 2, width: CGFloat(trimmedBmp.size.width), height: CGFloat(trimmedBmp.size.width)))!
//
//    // Assuming 'image' here represents the result image view
//    image.image = squareImg
//
//    // Close the image or do any cleanup needed
//}

@OptIn(ExperimentalForeignApi::class)
internal fun cropToSquare(uiImage: UIImage): UIImage {
    val imageRef = CGImageCreateCopyWithColorSpace(uiImage.CGImage, CGColorSpaceCreateDeviceRGB())

    val width = CGImageGetWidth(imageRef).toDouble()
    val height = CGImageGetHeight(imageRef).toDouble()

    val rect = CGRectMake(0.0, (height- width) / 2, width, width)
    val croppedImage = CGImageCreateWithImageInRect(imageRef, rect)

    val img = UIImage(croppedImage)
    return img
}

@OptIn(ExperimentalForeignApi::class)
internal fun UIImage.toSkiaImage(): Image? {
    val imageRef = CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceRGB()) ?: return null

    val width = CGImageGetWidth(imageRef).toInt()
    val height = CGImageGetHeight(imageRef).toInt()

    val bytesPerRow = CGImageGetBytesPerRow(imageRef)
    val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
    val bytePointer = CFDataGetBytePtr(data)
    val length = CFDataGetLength(data)
    val alphaInfo = CGImageGetAlphaInfo(imageRef)

    val alphaType = when (alphaInfo) {
        CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
        CGImageAlphaInfo.kCGImageAlphaFirst, CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
        CGImageAlphaInfo.kCGImageAlphaNone, CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst, CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
        else -> ColorAlphaType.UNKNOWN
    }

    val byteArray = ByteArray(length.toInt()) { index ->
        bytePointer!![index].toByte()
    }
    CFRelease(data)
    CFRelease(imageRef)

    return Image.makeRaster(
        imageInfo = ImageInfo(width = width, height = height, colorType = ColorType.RGBA_8888, alphaType = alphaType),
        bytes = byteArray,
        rowBytes = bytesPerRow.toInt(),
    )
}