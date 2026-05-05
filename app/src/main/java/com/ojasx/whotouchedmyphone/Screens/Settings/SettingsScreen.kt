package com.ojasx.whotouchedmyphone.Screens.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun SettingsScreen() {

    val switchState1 = remember { mutableStateOf(true) }
    val switchState2 = remember { mutableStateOf(true) }
    val switchState3 = remember { mutableStateOf(true) }
    val switchState4 = remember { mutableStateOf(true) }

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
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(36.dp))

        SettingsTopBar()

        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            GlassSettingsItem(
                icon = Icons.Default.Lock,
                text = "Change PIN",
                hasSwitch = false
            )

            GlassSettingsItem(
                icon = Icons.Default.Security,
                text = "Intruder Detection",
                hasSwitch = true,
                switchState = switchState1
            )

            GlassSettingsItem(
                icon = Icons.Default.Email,
                text = "Email Alerts",
                hasSwitch = true,
                switchState = switchState2
            )

            GlassSettingsItem(
                icon = Icons.Default.VolumeUp,
                text = "Sound on Wrong Attempt",
                hasSwitch = true,
                switchState = switchState3
            )

            GlassSettingsItem(
                icon = Icons.Default.DarkMode,
                text = "Dark Mode",
                hasSwitch = true,
                switchState = switchState4
            )

            GlassSettingsItem(
                icon = Icons.Default.Help,
                text = "Security Question",
                hasSwitch = false
            )

            GlassSettingsItem(
                icon = Icons.Default.Info,
                text = "About App",
                hasSwitch = false
            )
        }
    }
}