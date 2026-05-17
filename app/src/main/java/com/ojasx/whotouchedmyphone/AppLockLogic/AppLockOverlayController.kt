package com.ojasx.whotouchedmyphone.AppLockLogic

import android.accessibilityservice.AccessibilityService
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.ojasx.whotouchedmyphone.CameraXManager
import com.ojasx.whotouchedmyphone.Password.AppLockScreen
import com.ojasx.whotouchedmyphone.R
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PIN.PinRepository
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModelFactory
import com.ojasx.whotouchedmyphone.ui.theme.WhoTouchedMyPhoneTheme
import kotlin.math.max
import kotlin.math.min

/**
 * Full-screen accessibility overlay. Stays visible until [hideImmediate] after successful PIN.
 */
class AppLockOverlayController(
    private val service: AccessibilityService,
    private val onUnlocked: (String) -> Unit,
) {

    private val windowManager =
        service.getSystemService(AccessibilityService.WINDOW_SERVICE) as WindowManager

    private var overlayRoot: android.view.View? = null
    private var overlayParams: WindowManager.LayoutParams? = null
    private var showingPackage: String? = null
    private var overlayLifecycle: OverlayLifecycleOwner? = null
    private var cameraXManager: CameraXManager? = null

    fun isShowing(): Boolean = overlayRoot != null

    fun isShowingFor(packageName: String): Boolean =
        overlayRoot != null && showingPackage == packageName

    /**
     * Idempotent: keeps a single overlay up for [lockedPackageName] without remove/add flicker.
     */
    fun ensureShown(lockedPackageName: String) {
        if (isShowingFor(lockedPackageName)) {
            applyPortraitParams()
            return
        }
        if (isShowing()) {
            hideImmediate()
        }
        attachOverlay(lockedPackageName)
    }

    private fun attachOverlay(lockedPackageName: String) {
        val lifecycleOwner = OverlayLifecycleOwner().also {
            it.onOverlayAttached()
            overlayLifecycle = it
        }

        val inflater = LayoutInflater.from(service)
        val root = inflater.inflate(R.layout.lock_screen_overlay, null)
        val composeView = root.findViewById<ComposeView>(R.id.compose_lock_view)

        bindOverlayLifecycle(root, lifecycleOwner)

        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
        )

        val params = createPortraitOverlayParams()
        overlayParams = params

        val database = AppDatabase.getDatabase(service)
        val repository = PinRepository(database.pinDao())
        val pinViewModel = ViewModelProvider(
            lifecycleOwner,
            PinViewModelFactory(repository),
        )[PinViewModel::class.java]

        val cameraManager = CameraXManager(
            context = service,
            lifecycleOwner = lifecycleOwner,
        ).also { cameraXManager = it }

        windowManager.addView(root, params)

        composeView.setContent {
            WhoTouchedMyPhoneTheme {
                AppLockScreen(
                    pinViewModel = pinViewModel,
                    cameraXManager = cameraManager,
                    lockedPackageName = lockedPackageName,
                    onUnlockSuccess = {
                        hideImmediate()
                        onUnlocked(lockedPackageName)
                    },
                )
            }
        }

        overlayRoot = root
        showingPackage = lockedPackageName
        AppLockAccessibilityService.isLockScreenShowing = true
        AppLockAccessibilityService.currentLockedPackage = lockedPackageName
    }

    private fun applyPortraitParams() {
        val root = overlayRoot ?: return
        val params = overlayParams ?: return
        updatePortraitDimensions(params)
        try {
            windowManager.updateViewLayout(root, params)
        } catch (_: IllegalArgumentException) {
            // View detached
        }
    }

    private fun createPortraitOverlayParams(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_SECURE or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            PixelFormat.TRANSLUCENT,
        )
        params.gravity = Gravity.CENTER
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        updatePortraitDimensions(params)
        return params
    }

    /** Force portrait footprint even when the phone is physically in landscape. */
    private fun updatePortraitDimensions(params: WindowManager.LayoutParams) {
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val shortSide = min(metrics.widthPixels, metrics.heightPixels)
        val longSide = max(metrics.widthPixels, metrics.heightPixels)
        params.width = shortSide
        params.height = longSide
        if (service.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun bindOverlayLifecycle(
        root: android.view.View,
        lifecycleOwner: OverlayLifecycleOwner,
    ) {
        root.setViewTreeLifecycleOwner(lifecycleOwner)
        root.setViewTreeViewModelStoreOwner(lifecycleOwner)
        root.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        root.isFocusable = true
        root.isFocusableInTouchMode = true
    }

    fun hideImmediate() {
        val view = overlayRoot ?: return
        try {
            windowManager.removeView(view)
        } catch (_: IllegalArgumentException) {
            // Already removed
        }
        cameraXManager?.releaseCamera()
        cameraXManager = null
        overlayLifecycle?.onOverlayDetached()
        overlayLifecycle = null
        overlayRoot = null
        overlayParams = null
        showingPackage = null
        AppLockAccessibilityService.isLockScreenShowing = false
        AppLockAccessibilityService.currentLockedPackage = null
    }
}
