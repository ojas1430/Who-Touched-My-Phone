package com.ojasx.whotouchedmyphone.AppLockLogic

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.ojasx.whotouchedmyphone.AppLockLogic.AppLockManager
import com.ojasx.whotouchedmyphone.AppLockLogic.LockScreenActivity

class AppLockAccessibilityService : AccessibilityService() {

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

        // Ignore system UI
        if (packageName == "com.android.systemui") {
            return
        }

        val manager = AppLockManager(this)

        val isLocked = manager.isAppLocked(packageName)

        //  Prevent reopening lock repeatedly
        if (isLocked && lastLockedPackage != packageName) {

            lastLockedPackage = packageName

            val intent = Intent(this, LockScreenActivity::class.java).apply {

                // 🔥 Pass app package name
                putExtra("PACKAGE_NAME", packageName)

                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            }

            startActivity(intent)
        }

        // 🔥 Reset when user leaves app
        if (packageName != lastLockedPackage) {
            lastLockedPackage = ""
        }
    }

    override fun onInterrupt() {}
}