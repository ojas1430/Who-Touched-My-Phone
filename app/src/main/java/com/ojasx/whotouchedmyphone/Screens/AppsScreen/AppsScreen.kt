package com.ojasx.whotouchedmyphone.Screens.AppsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.FetchInstalledApps.AppListScreen
import com.ojasx.whotouchedmyphone.ViewModel.AppLockViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel

@Composable
fun AppsScreen(
    appLockViewModel: AppLockViewModel,
    pinViewModel: PinViewModel
) {

    Box(
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
    ) {

        Scaffold(
            containerColor = Color.Transparent,
            topBar = { AppTopBar() }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp)
            ) {

                AppListScreen(
                    appLockViewModel = appLockViewModel,
                    onAppClick = {
                        // no-op
                    }
                )
            }
        }
    }
}