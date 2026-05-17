package com.ojasx.whotouchedmyphone

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
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
    private val lifecycleOwner: LifecycleOwner,
) {

    private var imageCapture: ImageCapture? = null

    @Volatile
    private var isCapturing = false

    /** For Activity-based flows that show a preview (unchanged behavior). */
    fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                imageCapture = ImageCapture.Builder().build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageCapture,
                )
                Log.d("CAMERA_X", "Camera started")
            } catch (e: Exception) {
                Log.e("CAMERA_X_ERROR", "startCamera failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * Binds the camera only for a single capture (used on the accessibility overlay).
     * Avoids keeping the camera open while the PIN screen is visible, which caused broken-pipe errors.
     */
    fun takePhoto(
        packageName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
    ) {
        if (isCapturing) return
        isCapturing = true

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val capture = ImageCapture.Builder().build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    capture,
                )
                imageCapture = capture
                takePhotoInternal(packageName, onSuccess, onError) {
                    try {
                        cameraProvider.unbindAll()
                    } catch (e: Exception) {
                        Log.w("CAMERA_X", "unbind after capture", e)
                    }
                    imageCapture = null
                    isCapturing = false
                }
            } catch (e: Exception) {
                isCapturing = false
                Log.e("CAMERA_X_ERROR", "takePhoto bind failed", e)
                onError(e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun releaseCamera() {
        try {
            ProcessCameraProvider.getInstance(context).get().unbindAll()
        } catch (e: Exception) {
            Log.w("CAMERA_X", "releaseCamera", e)
        }
        imageCapture = null
        isCapturing = false
    }

    private fun takePhotoInternal(
        packageName: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,
        onFinished: () -> Unit,
    ) {
        val capture = imageCapture
        if (capture == null) {
            onFinished()
            onError(IllegalStateException("Camera not ready"))
            return
        }

        val appName = getAppNameFromPackage(packageName)
        val fileName = "${packageName}_${System.currentTimeMillis()}"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/WhoTouchedMyPhone",
            )
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues,
        ).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri?.toString() ?: ""
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getDatabase(context).intruderLogDao().insertLog(
                            IntruderLog(
                                imageUri = savedUri,
                                appPackage = packageName,
                                appName = appName,
                                timestamp = System.currentTimeMillis(),
                            ),
                        )
                    }
                    onFinished()
                    onSuccess()
                }

                override fun onError(exception: ImageCaptureException) {
                    onFinished()
                    onError(exception)
                }
            },
        )
    }

    fun getAppNameFromPackage(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}
