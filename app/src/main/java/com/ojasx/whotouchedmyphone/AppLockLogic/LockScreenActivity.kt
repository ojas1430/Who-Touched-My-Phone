package com.ojasx.whotouchedmyphone.AppLockLogic

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.ojasx.whotouchedmyphone.CameraXManager
import com.ojasx.whotouchedmyphone.Password.LockScreen
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PIN.PinRepository
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModelFactory

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Locked app package
        val packageNameToOpen =
            intent.getStringExtra("PACKAGE_NAME") ?: ""

        // Database
        val database =
            AppDatabase.getDatabase(this)

        // Repository
        val repository =
            PinRepository(database.pinDao())

        // Factory
        val factory =
            PinViewModelFactory(repository)

        // ViewModel
        val pinViewModel =
            ViewModelProvider(
                this,
                factory
            )[PinViewModel::class.java]

        // CameraX
        val cameraXManager =
            CameraXManager(
                context = this,
                lifecycleOwner = this
            )

        // Camera permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            100
        )

        setContent {

            val previewView = remember {
                PreviewView(this)
            }

            // Start hidden camera
            LaunchedEffect(Unit) {

                cameraXManager.startCamera(
                    previewView
                )
            }

            // Hidden PreviewView
            AndroidView(

                factory = {
                    previewView
                },

                modifier = Modifier.size(1.dp)
            )

            LockScreen(

                pinViewModel = pinViewModel,

                cameraXManager = cameraXManager,

                lockedPackageName = packageNameToOpen,

                onUnlockSuccess = {

                    AppLockAccessibilityService
                        .temporarilyUnlockedPackage =
                        packageNameToOpen

                    AppLockAccessibilityService
                        .isLockScreenShowing = false

                    // Open locked app
                    val launchIntent =
                        packageManager
                            .getLaunchIntentForPackage(
                                packageNameToOpen
                            )

                    launchIntent?.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )

                    if (launchIntent != null) {

                        startActivity(
                            launchIntent
                        )
                    }

                    finish()
                }
            )
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        AppLockAccessibilityService
            .isLockScreenShowing = false
    }
}