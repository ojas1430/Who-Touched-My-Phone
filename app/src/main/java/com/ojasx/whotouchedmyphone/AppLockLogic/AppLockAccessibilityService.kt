package com.ojasx.whotouchedmyphone.AppLockLogic

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AppLockAccessibilityService : AccessibilityService() {

    companion object {

        // App unlocked temporarily after correct PIN
        var temporarilyUnlockedPackage: String? = null

        // Prevent multiple lock screens
        var isLockScreenShowing = false
    }

    private var lastLockedPackage = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val packageName = event.packageName?.toString() ?: return

        // Ignore your own app
        if (packageName == this.packageName) {
            return
        }

        // Ignore system ui
        if (packageName == "com.android.systemui") {
            return
        }

        // Already unlocked
        if (packageName == temporarilyUnlockedPackage) {
            return
        }

        val manager = AppLockManager(this)

        val isLocked = manager.isAppLocked(packageName)

        // Prevent repeated lock screen
        if (
            isLocked &&
            lastLockedPackage != packageName &&
            !isLockScreenShowing
        ) {

            lastLockedPackage = packageName

            isLockScreenShowing = true

            val intent = Intent(this, LockScreenActivity::class.java).apply {

                putExtra("PACKAGE_NAME", packageName)

                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            }

            startActivity(intent)
        }

        // Reset when user leaves app
        if (packageName != lastLockedPackage) {

            lastLockedPackage = ""

            if (packageName != temporarilyUnlockedPackage) {
                temporarilyUnlockedPackage = null
            }
        }
    }

    override fun onInterrupt() {}
}