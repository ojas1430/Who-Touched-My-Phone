package com.ojasx.whotouchedmyphone.Permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import kotlinx.coroutines.delay

@Composable
fun PermissionSetupScreen(
    onAllPermissionsGranted: () -> Unit
) {

    val context = LocalContext.current

    var overlayEnabled by remember {
        mutableStateOf(false)
    }

    var batteryEnabled by remember {
        mutableStateOf(false)
    }

    var accessibilityEnabled by remember {
        mutableStateOf(false)
    }

    var cameraEnabled by remember {
        mutableStateOf(false)
    }

    var mediaEnabled by remember {
        mutableStateOf(false)
    }

    var showAccessibilityInfoDialog by remember {
        mutableStateOf(false)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraEnabled = isGranted
    }

    val mediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        mediaEnabled = isGranted
    }

    // Auto refresh permissions
    LaunchedEffect(Unit) {

        while (true) {

            overlayEnabled =
                isOverlayPermissionGranted(context)

            batteryEnabled =
                isIgnoringBatteryOptimizations(context)

            accessibilityEnabled =
                isAccessibilityServiceEnabled(context)

            cameraEnabled =
                isCameraPermissionGranted(context)

            mediaEnabled =
                isMediaPermissionGranted(context)

            delay(1000)
        }
    }

    val allGranted =
        overlayEnabled &&
                batteryEnabled &&
                accessibilityEnabled &&
                cameraEnabled &&
                mediaEnabled

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        containerColor = Color.Transparent,

        contentWindowInsets = WindowInsets.systemBars

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0B0E17),
                            Color(0xFF111528),
                            Color(0xFF0B0E17)
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Setup App Lock",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enable all permissions to protect your apps.",
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Info Button
            Text(

                text = "Why Accessibility Permission?",

                color = Color(0xFF7B61FF),

                fontWeight = FontWeight.SemiBold,

                modifier = Modifier.clickable {

                    showAccessibilityInfoDialog = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(

                verticalArrangement = Arrangement.spacedBy(16.dp),

                modifier = Modifier.weight(1f)
            ) {

                item {

                    PermissionCard(

                        title = "Accessibility Permission",

                        description =
                            "Required to detect opened apps and lock them securely.",

                        granted = accessibilityEnabled,

                        onClick = {

                            context.startActivity(
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            )
                        }
                    )
                }

                item {

                    PermissionCard(

                        title = "Camera Permission",

                        description =
                        "Required to take a photo of the intruder.",

                        granted = cameraEnabled,

                        onClick = {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }

                item {

                    PermissionCard(

                        title = "Storage Permission",

                        description =
                        "Required to save and show intrusion logs.",

                        granted = mediaEnabled,

                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                mediaLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                mediaLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }
                    )
                }

                item {

                    PermissionCard(

                        title = "Display Over Other Apps",

                        description =
                            "Required to show lock screen above apps.",

                        granted = overlayEnabled,

                        onClick = {

                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                "package:${context.packageName}".toUri()
                            )

                            context.startActivity(intent)
                        }
                    )
                }

                item {

                    PermissionCard(

                        title = "Ignore Battery Optimization",

                        description =
                            "Prevents Android from stopping the app lock service.",

                        granted = batteryEnabled,

                        onClick = {

                            val intent = Intent(
                                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                                Uri.parse("package:${context.packageName}")
                            )

                            context.startActivity(intent)
                        }
                    )
                }

                item {

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                }
            }

            Button(

                onClick = {

                    if (allGranted) {

                        onAllPermissionsGranted()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
                    .height(58.dp),

                enabled = allGranted,

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(

                    containerColor =
                        if (allGranted)
                            Color(0xFF7B61FF)
                        else
                            Color(0xFF3A315E),

                    contentColor = Color.White,

                    disabledContainerColor = Color(0xFF2A2438),

                    disabledContentColor = Color.LightGray
                )

            ) {

                Text(

                    text =
                        if (allGranted)
                            "Continue"
                        else
                            "Enable All Permissions",

                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Accessibility Info Dialog
        if (showAccessibilityInfoDialog) {

            AlertDialog(

                onDismissRequest = {

                    showAccessibilityInfoDialog = false
                },

                confirmButton = {

                    TextButton(

                        onClick = {

                            showAccessibilityInfoDialog = false
                        }
                    ) {

                        Text(
                            text = "Got it"
                        )
                    }
                },

                title = {

                    Text(
                        text = "Accessibility Permission"
                    )
                },

                text = {

                    Column {

                        Text(
                            text =
                                "This app uses Accessibility Service only to:"
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "• Detect opened apps"
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "• Lock selected apps with PIN"
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "• Prevent unauthorized access"
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text =
                                "We do NOT collect, store, or share personal data.",
                            color = Color.Gray
                        )
                    }
                }
            )
        }
    }
}

fun isOverlayPermissionGranted(context: Context): Boolean {

    return Settings.canDrawOverlays(context)
}

fun isIgnoringBatteryOptimizations(context: Context): Boolean {

    val powerManager =
        context.getSystemService(Context.POWER_SERVICE) as PowerManager

    return powerManager.isIgnoringBatteryOptimizations(
        context.packageName
    )
}

fun isAccessibilityServiceEnabled(context: Context): Boolean {

    val enabledServices = Settings.Secure.getString(

        context.contentResolver,

        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES

    ) ?: return false

    return enabledServices.contains(
        context.packageName
    )
}

fun isCameraPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun isMediaPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}
