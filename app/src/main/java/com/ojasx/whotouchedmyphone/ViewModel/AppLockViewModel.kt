package com.ojasx.whotouchedmyphone.ViewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.ojasx.whotouchedmyphone.AppLockLogic.AppLockManager

class AppLockViewModel(
    private val manager: AppLockManager
) : ViewModel() {

    // Compose observable state
    private val lockedAppsState = mutableStateMapOf<String, Boolean>()

    init {
        // optional preload
    }

    fun isLocked(packageName: String): Boolean {

        // First check compose state
        return lockedAppsState[packageName]
            ?: manager.isAppLocked(packageName)
    }

    fun toggleLock(packageName: String) {

        val currentlyLocked = manager.isAppLocked(packageName)

        if (currentlyLocked) {
            manager.unlockApp(packageName)
            lockedAppsState[packageName] = false
        } else {
            manager.lockApp(packageName)
            lockedAppsState[packageName] = true
        }
    }

    fun lock(packageName: String) {
        manager.lockApp(packageName)
        lockedAppsState[packageName] = true
    }

    fun unlock(packageName: String) {
        manager.unlockApp(packageName)
        lockedAppsState[packageName] = false
    }
}