package com.ojasx.whotouchedmyphone.FetchInstalledApps

import android.content.Context
import android.content.Intent

fun getInstalledApps(context: Context): List<AppInfo> {

    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    val resolveInfoList = pm.queryIntentActivities(intent, 0)

    return resolveInfoList.map { resolveInfo ->

        val appName = resolveInfo.loadLabel(pm).toString()
        val packageName = resolveInfo.activityInfo.packageName
        val icon = resolveInfo.loadIcon(pm)

        AppInfo(
            appName = appName,
            packageName = packageName,
            icon = icon
        )
    }.sortedBy { it.appName.lowercase() }
}