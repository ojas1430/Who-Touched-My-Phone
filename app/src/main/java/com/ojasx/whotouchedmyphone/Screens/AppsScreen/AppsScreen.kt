package com.ojasx.whotouchedmyphone.Screens.AppsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.FetchInstalledApps.AppListScreen
import com.ojasx.whotouchedmyphone.ViewModel.AppLockViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel
import kotlinx.coroutines.delay

@Composable
fun AppsScreen(
    appLockViewModel: AppLockViewModel,
    pinViewModel: PinViewModel
) {

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {

        // Fake loading delay for smooth UX
        delay(1200)

        isLoading = false
    }

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

        if (isLoading) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator(
                    color = Color(0xFF7B61FF)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Loading apps...",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

        } else {

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
}