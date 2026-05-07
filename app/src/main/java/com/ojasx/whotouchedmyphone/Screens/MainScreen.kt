package com.ojasx.whotouchedmyphone.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ojasx.whotouchedmyphone.Screens.AppsScreen.AppsScreen
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.DashboardScreen
import com.ojasx.whotouchedmyphone.Screens.Logs.LogsScreen
import com.ojasx.whotouchedmyphone.Screens.Settings.SettingsScreen

@Composable
fun MainScreen(navController: NavController) {

    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {

        when (selectedIndex) {
            0 -> DashboardScreen()
            1 -> LogsScreen()
            2 -> SettingsScreen()
            4 -> AppsScreen()
        }

        // 👇 THIS is what you were missing
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = {
                    selectedIndex = it
                }
            )
        }
    }
}