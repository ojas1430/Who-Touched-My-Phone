package com.ojasx.whotouchedmyphone.AppLockLogic

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

/**
 * Locks third-party apps with a portrait accessibility overlay that stays until PIN unlock.
 */
class AppLockAccessibilityService : AccessibilityService() {

    companion object {
        @Volatile
        var sessionUnlockedPackage: String? = null

        @Volatile
        var isLockScreenShowing: Boolean = false

        @Volatile
        var currentLockedPackage: String? = null

        private const val WATCHDOG_INTERVAL_MS = 200L
        private const val LEAVE_APP_CONFIRM_MS = 500L

        private val LAUNCHER_PACKAGES = setOf(
            "com.android.launcher",
            "com.android.launcher3",
            "com.google.android.apps.nexuslauncher",
            "com.miui.home",
            "com.sec.android.app.launcher",
            "com.huawei.android.launcher",
            "com.oppo.launcher",
            "com.oneplus.launcher",
        )

        private val TRANSIENT_SYSTEM_PACKAGES = setOf(
            "com.android.systemui",
            "android",
        )
    }

    private val lockManager by lazy { AppLockManager(this) }
    private val mainHandler = Handler(Looper.getMainLooper())
    private var overlayController: AppLockOverlayController? = null

    /** Locked app we are currently protecting (until user leaves or unlocks). */
    private var lockTargetPackage: String? = null

    private var pendingLeaveApp: Runnable? = null
    private var watchdogRunnable: Runnable? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        overlayController = AppLockOverlayController(
            service = this,
            onUnlocked = { packageName ->
                sessionUnlockedPackage = packageName
                lockTargetPackage = packageName
                stopWatchdog()
            },
        )
    }

    override fun onDestroy() {
        stopWatchdog()
        cancelPendingLeave()
        overlayController?.hideImmediate()
        sessionUnlockedPackage = null
        lockTargetPackage = null
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageName = event.packageName?.toString() ?: return
        val overlay = overlayController ?: return


        if (isTransientSystemPackage(packageName)) {
            if (mustKeepOverlayUp()) {
                cancelPendingLeave()
                overlay.ensureShown(lockTargetPackage!!)
                startWatchdog(lockTargetPackage!!)
            }
            return
        }

        if (isLauncher(packageName)) {
            onLauncherForeground(overlay)
            return
        }

        if (!lockManager.isAppLocked(packageName)) {
            if (mustKeepOverlayUp() && packageName != lockTargetPackage) {
                return
            }
            if (overlay.isShowing()) {
                return
            }
            lastUnlockedForeground(packageName)
            return
        }

        onLockedAppForeground(packageName, overlay)
    }

    override fun onInterrupt() {
        stopWatchdog()
        cancelPendingLeave()
        overlayController?.hideImmediate()
        lockTargetPackage = null
    }

    private fun onLockedAppForeground(packageName: String, overlay: AppLockOverlayController) {
        cancelPendingLeave()

        if (sessionUnlockedPackage != null && sessionUnlockedPackage != packageName) {
            sessionUnlockedPackage = null
        }

        lockTargetPackage = packageName

        if (packageName == sessionUnlockedPackage) {
            stopWatchdog()
            if (overlay.isShowing()) {
                overlay.hideImmediate()
            }
            return
        }

        overlay.ensureShown(packageName)
        startWatchdog(packageName)
    }

    private fun onLauncherForeground(overlay: AppLockOverlayController) {
        if (!mustKeepOverlayUp()) {
            sessionUnlockedPackage = null
            lockTargetPackage = null
            stopWatchdog()
            return
        }

        cancelPendingLeave()
        pendingLeaveApp = Runnable {
            if (lockTargetPackage == null) return@Runnable
            sessionUnlockedPackage = null
            lockTargetPackage = null
            stopWatchdog()
            overlay.hideImmediate()
        }
        mainHandler.postDelayed(pendingLeaveApp!!, LEAVE_APP_CONFIRM_MS)
    }

    private fun lastUnlockedForeground(packageName: String) {
        if (lockTargetPackage != null && lockTargetPackage != packageName) {
            sessionUnlockedPackage = null
            lockTargetPackage = null
            stopWatchdog()
            overlayController?.hideImmediate()
        }
    }

    private fun mustKeepOverlayUp(): Boolean {
        val target = lockTargetPackage ?: return false
        if (!lockManager.isAppLocked(target)) return false
        return target != sessionUnlockedPackage
    }

    private fun startWatchdog(packageName: String) {
        stopWatchdog()
        watchdogRunnable = object : Runnable {
            override fun run() {
                if (packageName != lockTargetPackage) return
                if (packageName == sessionUnlockedPackage) {
                    stopWatchdog()
                    return
                }
                if (!lockManager.isAppLocked(packageName)) {
                    stopWatchdog()
                    return
                }
                overlayController?.ensureShown(packageName)
                mainHandler.postDelayed(this, WATCHDOG_INTERVAL_MS)
            }
        }
        mainHandler.post(watchdogRunnable!!)
    }

    private fun stopWatchdog() {
        watchdogRunnable?.let { mainHandler.removeCallbacks(it) }
        watchdogRunnable = null
    }

    private fun cancelPendingLeave() {
        pendingLeaveApp?.let { mainHandler.removeCallbacks(it) }
        pendingLeaveApp = null
    }

    private fun isLauncher(packageName: String): Boolean =
        packageName in LAUNCHER_PACKAGES ||
            packageName.contains("launcher", ignoreCase = true)

    private fun isTransientSystemPackage(packageName: String): Boolean =
        packageName in TRANSIENT_SYSTEM_PACKAGES
}
