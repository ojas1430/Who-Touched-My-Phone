package com.ojasx.whotouchedmyphone.AppLockLogic

import android.content.Context

class AppLockManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("locked_apps", Context.MODE_PRIVATE)

    fun lockApp(packageName: String) {
        prefs.edit().putBoolean(packageName, true).apply()
    }

    fun unlockApp(packageName: String) {
        prefs.edit().remove(packageName).apply()
    }

    fun isAppLocked(packageName: String): Boolean {
        return prefs.getBoolean(packageName, false)
    }
}