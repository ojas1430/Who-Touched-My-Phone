package com.ojasx.whotouchedmyphone.Screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.LastIntrusionCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ProtectedCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ThisMonthCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ThisWeekCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.TotalIntrusionsCard


@Preview
@Composable
fun DashboardScreen() {

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
            topBar = { DashboardTopBar() }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // 🔥 THIS is the key change
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f) // 👈 takes 70% of screen
                ) {

                    ProtectedCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            TotalIntrusionsCard()
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            LastIntrusionCard()
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            ThisWeekCard()
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            ThisMonthCard()
                        }
                    }
                }

                // Optional: remaining space (30%)
                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
    }
}