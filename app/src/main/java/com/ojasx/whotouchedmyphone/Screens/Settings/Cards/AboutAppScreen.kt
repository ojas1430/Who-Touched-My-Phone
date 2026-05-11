package com.ojasx.whotouchedmyphone.Screens.Settings.Cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(
    navController: NavController
) {

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        containerColor = Color.Transparent,

        contentWindowInsets = WindowInsets.systemBars,

        topBar = {

            TopAppBar(

                modifier = Modifier.statusBarsPadding(),

                navigationIcon = {

                    IconButton(

                        onClick = {

                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription = null,

                            tint = Color.White
                        )
                    }
                },

                title = {

                    Text(
                        text = "About App",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0B0E17)
                )
            )
        }

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
                .navigationBarsPadding()
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(

                text = "Who Touched My Phone",

                style = MaterialTheme.typography.headlineMedium,

                color = Color.White,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(

                text =
                    "Who Touched My Phone is a privacy and security application designed to protect your personal apps from unauthorized access.",

                color = Color.LightGray
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            AboutSection(

                title = "Features",

                content =
                    "• App Lock Protection\n\n" +
                            "• Intruder Selfie Detection\n\n" +
                            "• Intrusion Logs\n\n" +
                            "• PIN Security\n\n" +
                            "• Real-Time Protection"
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            AboutSection(

                title = "Privacy",

                content =
                    "Your data stays on your device. The app does not collect or share personal information."
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            AboutSection(

                title = "Version",

                content = "Version 1.0.0"
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )
        }
    }
}

@Composable
fun AboutSection(

    title: String,

    content: String
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1A1F2E),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {

        Text(

            text = title,

            color = Color.White,

            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(

            text = content,

            color = Color.LightGray
        )
    }
}