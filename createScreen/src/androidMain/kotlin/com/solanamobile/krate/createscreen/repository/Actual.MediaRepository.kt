package com.solanamobile.krate.createscreen.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.moriatsushi.koject.Provides
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Provides
actual class MediaRepository(
    private val context: Context
) {
    actual fun imageBitmapToByteArray(bmp: ImageBitmap): ByteArray {
        val androidBitmap = bmp.asAndroidBitmap()

        val stream = ByteArrayOutputStream()
        androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val bytes = stream.toByteArray()
        androidBitmap.recycle()

        return bytes
    }

    actual fun saveBitmap(bmp: ImageBitmap) {
        val filename = "KRATE_${getDateTimeString()}.png"

        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)

        var writingImgUri: Uri? = null
        try {
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also { uri ->
                writingImgUri = uri

                context.contentResolver.openOutputStream(uri)?.let { stream ->
                    bmp.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
            }
        } catch (e: Exception) {
            writingImgUri?.let { cleanupUri ->
                context.contentResolver.delete(cleanupUri, null, null)
            }

            throw e
        }
    }

    private fun getDateTimeString(): String {
        val dateFormat = SimpleDateFormat("MM_dd_yy_HHmmss", Locale.getDefault())

        return dateFormat.format(Date())
    }
}