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
import com.ojasx.whotouchedmyphone.RoomDb.Logs.IntruderLog
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraXManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private var imageCapture: ImageCapture? = null

    fun startCamera(previewView: PreviewView) {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            try {

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                Log.d("CAMERA_X", "Camera Started Successfully")

            } catch (e: Exception) {

                Log.e("CAMERA_X_ERROR", e.message.toString())
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(
        packageName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val imageCapture = imageCapture

        if (imageCapture == null) {

            Toast.makeText(context, "Camera Not Ready", Toast.LENGTH_SHORT).show()
            return
        }

        val appName = getAppNameFromPackage(packageName)

        val fileName = "${packageName}_${System.currentTimeMillis()}"

        val contentValues = ContentValues().apply {

            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/WhoTouchedMyPhone"
            )
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
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

                    val savedUri =
                        outputFileResults.savedUri?.toString() ?: ""

                    CoroutineScope(Dispatchers.IO).launch {

                        AppDatabase
                            .getDatabase(context)
                            .intruderLogDao()
                            .insertLog(

                                IntruderLog(

                                    imageUri = savedUri,

                                    appPackage = packageName,

                                    appName = appName,

                                    timestamp = System.currentTimeMillis()
                                )
                            )
                    }

                    onSuccess()
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {

                    onError(exception)
                }
            }
        )
    }

    fun getAppNameFromPackage(
        packageName: String
    ): String {

        return try {

            val appInfo =
                context.packageManager
                    .getApplicationInfo(packageName, 0)

            context.packageManager
                .getApplicationLabel(appInfo)
                .toString()

        } catch (e: Exception) {

            packageName
        }
    }
}