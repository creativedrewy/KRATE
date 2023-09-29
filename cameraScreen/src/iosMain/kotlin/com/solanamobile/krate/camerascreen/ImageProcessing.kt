package com.solanamobile.krate.camerascreen

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
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGContextRotateCTM
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateCopyWithColorSpace
import platform.CoreGraphics.CGImageCreateWithImageInRect
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetColorSpace
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIImage
import kotlin.math.PI

val Int.uL
    get() = this.toULong()

val Double.uL
    get() = this.toULong()

@OptIn(ExperimentalForeignApi::class)
internal fun rotateUprightAndSquareCrop(uiImage: UIImage): UIImage {
    val cgImage = uiImage.CGImage!!

    val width = CGImageGetWidth(cgImage).toDouble()
    val height = CGImageGetHeight(cgImage).toDouble()

    val rotatedImg = if (width > height) {
        val colorSpace = CGImageGetColorSpace(cgImage)
        val context = CGBitmapContextCreate(null, width.uL, height.uL, 8.uL, 0.uL, colorSpace, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value)

        CGContextTranslateCTM(context, width / 2, height / 2);
        CGContextRotateCTM(context, -PI / 2);
        CGContextTranslateCTM(context, -width / 2, -height / 2);
        CGContextDrawImage(context, CGRectMake(0.0, 0.0, width, height), cgImage);

        val rotatedImageRef = CGBitmapContextCreateImage(context);
        rotatedImageRef
    } else {
        throw Exception("Invalid image aspect ratio")
    }

    val rotW = CGImageGetWidth(rotatedImg).toDouble()
    val rotH = CGImageGetHeight(rotatedImg).toDouble()

    val rect = CGRectMake((rotW - rotH) / 2, 0.0, rotH, rotH)
    val croppedImage = CGImageCreateWithImageInRect(rotatedImg, rect)

    return UIImage.imageWithCGImage(croppedImage)
}

@OptIn(ExperimentalForeignApi::class)
internal fun UIImage.resize(width: Double, height: Double): UIImage {
    val targetSize = CGSizeMake(width, height)
    val currentSize = this.size

    val widthRatio = targetSize.useContents { width } / currentSize.useContents { width }
    val heightRatio = targetSize.useContents { height } / currentSize.useContents { height }

    val newSize: CValue<CGSize> = if (widthRatio > heightRatio) {
        CGSizeMake(currentSize.useContents { width } * heightRatio, currentSize.useContents { height } * heightRatio)
    } else {
        CGSizeMake(currentSize.useContents { width } * widthRatio, currentSize.useContents { height } * widthRatio)
    }

    val newW = newSize.useContents { width }
    val newH = newSize.useContents { height }

    val colorSpace = CGImageGetColorSpace(this.CGImage!!)
    val context = CGBitmapContextCreate(null, newW.uL, newH.uL, 8.uL, 0.uL, colorSpace, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value)

    CGContextScaleCTM(context, widthRatio, heightRatio)
    CGContextDrawImage(context, CGRectMake(0.0, 0.0, newW, newH), this.CGImage!!);

    val scaledImg = CGBitmapContextCreateImage(context);
    return UIImage.imageWithCGImage(scaledImg)
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