package com.ojasx.whotouchedmyphone.FetchInstalledApps

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.graphics.Canvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import com.ojasx.whotouchedmyphone.ViewModel.AppLockViewModel


@Composable
fun AppListScreen(
    appLockViewModel: AppLockViewModel,
    onAppClick: (AppInfo) -> Unit
) {
    val context = LocalContext.current
    val apps = remember { getInstalledApps(context) }

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(apps) { _, app ->

            val isLocked = appLockViewModel.isLocked(app.packageName)

            GlassAppItem(
                app = app,
                isLocked = isLocked,
                onAppClick = {
                    onAppClick(app)
                },
                onLockClick = {
                    appLockViewModel.toggleLock(app.packageName)
                }
            )
        }
    }
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) return bitmap

    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.coerceAtLeast(1),
        intrinsicHeight.coerceAtLeast(1),
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)

    return bitmap
}