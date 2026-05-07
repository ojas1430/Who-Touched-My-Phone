package com.ojasx.whotouchedmyphone.ViewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class AppLockViewModel : ViewModel() {

    private val _lockedApps = mutableStateMapOf<String, Boolean>()
    val lockedApps: Map<String, Boolean> = _lockedApps

    fun isLocked(packageName: String): Boolean {
        return _lockedApps[packageName] ?: false
    }

    fun toggleLock(packageName: String) {
        val current = _lockedApps[packageName] ?: false
        _lockedApps[packageName] = !current
    }

    fun lock(packageName: String) {
        _lockedApps[packageName] = true
    }

    fun unlock(packageName: String) {
        _lockedApps[packageName] = false
    }
}