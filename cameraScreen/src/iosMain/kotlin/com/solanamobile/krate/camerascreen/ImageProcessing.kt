package com.solanamobile.krate.camerascreen

import co.touchlab.kermit.Logger
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.useContents
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextRotateCTM
import platform.CoreGraphics.CGContextTranslateCTM
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
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import kotlin.math.PI

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
    val cgImage = uiImage.CGImage!!

    val width = CGImageGetWidth(cgImage).toDouble()
    val height = CGImageGetHeight(cgImage).toDouble()

//    UIGraphicsBeginImageContext(src.size);
//    CGContextRef context=(UIGraphicsGetCurrentContext());
//
//    if (orientation == UIImageOrientationRight) {
//        CGContextRotateCTM (context, 90/180*M_PI) ;
//    } else if (orientation == UIImageOrientationLeft) {
//        CGContextRotateCTM (context, -90/180*M_PI);
//    } else if (orientation == UIImageOrientationDown) {
//        // NOTHING
//    } else if (orientation == UIImageOrientationUp) {
//        CGContextRotateCTM (context, 90/180*M_PI);
//    }
//
//    [src drawAtPoint:CGPointMake(0, 0)];
//    UIImage *img=UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    return img;

    val rotatedImg = if (width > height) {
        UIGraphicsBeginImageContextWithOptions(uiImage.size, false, uiImage.scale)
        val gfxContext = UIGraphicsGetCurrentContext()

        CGContextTranslateCTM(gfxContext, width / 2, height / 2)
        CGContextRotateCTM(gfxContext, PI / 2)
        CGContextTranslateCTM(gfxContext, -width / 2, -height / 2)

        val rect = CGRectMake(0.0, 0.0, width, height)
        uiImage.drawInRect(rect)

        val rotatedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        rotatedImage!!.CGImage
    } else {
        throw Exception("Invalid image dimensions")
    }

    val rotW = CGImageGetWidth(rotatedImg).toDouble()
    val rotH = CGImageGetHeight(rotatedImg).toDouble()

    Logger.v { "::: Your dimensions: $rotW, $rotH" }

    val rect = CGRectMake((rotW - rotH) / 2, 0.0, rotH, rotH)
    val croppedImage = CGImageCreateWithImageInRect(rotatedImg, rect)

    val img = UIImage.imageWithCGImage(croppedImage)
    return img
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.resize(targetSize: CValue<CGSize>): UIImage {
    val currentSize = this.size

    val widthRatio = targetSize.useContents { width } / currentSize.useContents { width }
    val heightRatio = targetSize.useContents { height } / currentSize.useContents { height }

    val newSize: CValue<CGSize> = if (widthRatio > heightRatio) {
        CGSizeMake(currentSize.useContents { width } * heightRatio, currentSize.useContents { height } * heightRatio)
    } else {
        CGSizeMake(currentSize.useContents { width } * widthRatio, currentSize.useContents { height } * widthRatio)
    }

    val newRect = CGRectMake(0.0, 0.0, newSize.useContents { width }, newSize.useContents { height })

    UIGraphicsBeginImageContextWithOptions(size = newSize, opaque = false, scale = 1.0)
    this.drawInRect(newRect)
    val newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return newImage!!
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