package com.ojasx.whotouchedmyphone

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class CameraXManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private var imageCapture: ImageCapture? = null

    fun startCamera(previewView: PreviewView) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            try {

                val cameraProvider =
                    cameraProviderFuture.get()

                val preview =
                    Preview.Builder().build()

                preview.setSurfaceProvider(
                    previewView.surfaceProvider
                )

                imageCapture =
                    ImageCapture.Builder().build()

                val cameraSelector =
                    CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                Log.d(
                    "CAMERA_X",
                    "Camera Started Successfully"
                )

            } catch (e: Exception) {

                Log.e(
                    "CAMERA_X_ERROR",
                    e.message.toString()
                )

                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val imageCapture = imageCapture

        if (imageCapture == null) {

            Log.e(
                "CAMERA_X",
                "ImageCapture is NULL"
            )

            Toast.makeText(
                context,
                "Camera Not Ready",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val fileName =
            "IMG_${System.currentTimeMillis()}"

        val contentValues = ContentValues().apply {

            put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                fileName
            )

            put(
                MediaStore.MediaColumns.MIME_TYPE,
                "image/jpeg"
            )

            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/WhoTouchedMyPhone"
            )
        }

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture.takePicture(

            outputOptions,

            ContextCompat.getMainExecutor(context),

            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {

                    Log.d(
                        "CAMERA_X",
                        "PHOTO SAVED SUCCESSFULLY"
                    )

                    Log.d(
                        "CAMERA_X_URI",
                        outputFileResults.savedUri.toString()
                    )

                    Toast.makeText(
                        context,
                        "Photo Saved",
                        Toast.LENGTH_SHORT
                    ).show()

                    onSuccess()
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {

                    Log.e(
                        "CAMERA_X_ERROR",
                        exception.message.toString()
                    )

                    Toast.makeText(
                        context,
                        "Capture Failed",
                        Toast.LENGTH_SHORT
                    ).show()

                    onError(exception)
                }
            }
        )
    }
}