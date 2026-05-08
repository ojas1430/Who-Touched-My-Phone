package com.ojasx.whotouchedmyphone.AppLockLogic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.ojasx.whotouchedmyphone.Password.LockScreen
import com.ojasx.whotouchedmyphone.RoomDb.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PinRepository
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModelFactory

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Get app package name
        val packageNameToOpen =
            intent.getStringExtra("PACKAGE_NAME")

        // DB
        val database = AppDatabase.Companion.getDatabase(this)

        // Repository
        val repository = PinRepository(database.pinDao())

        // Factory
        val factory = PinViewModelFactory(repository)

        // ViewModel
        val pinViewModel = ViewModelProvider(
            this,
            factory
        )[PinViewModel::class.java]

        setContent {

            LockScreen(

                pinViewModel = pinViewModel,

                onUnlockSuccess = {

                    // 🔥 Open locked app after PIN success
                    if (packageNameToOpen != null) {

                        val launchIntent =
                            packageManager.getLaunchIntentForPackage(
                                packageNameToOpen
                            )

                        if (launchIntent != null) {

                            launchIntent.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )

                            startActivity(launchIntent)
                        }
                    }

                    finish()
                }
            )
        }
    }
}