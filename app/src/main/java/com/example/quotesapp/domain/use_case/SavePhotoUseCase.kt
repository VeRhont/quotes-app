package com.example.quotesapp.domain.use_case

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.quotesapp.domain.repository.PhotoRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject


class SavePhotoUseCase @Inject constructor(
    private val repository: PhotoRepository,
) {

    operator fun invoke(context: Context, bitmap: Bitmap) {

        val fileName = "photo-${System.currentTimeMillis()}.jpg"
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        "image/jpeg"
                    )
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val contentResolver = context.contentResolver

                contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )?.let {
                    outputStream = contentResolver.openOutputStream(it)
                }
            } else {
                val picturesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val imageFile = File(picturesDir, fileName)
                outputStream = FileOutputStream(imageFile)
            }

            outputStream?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.flush()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}