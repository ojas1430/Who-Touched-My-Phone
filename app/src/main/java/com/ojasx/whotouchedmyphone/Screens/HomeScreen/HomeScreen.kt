package com.ojasx.whotouchedmyphone.Screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.IntrusionStats
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.LastIntrusionCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ProtectedCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ThisMonthCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.ThisWeekCard
import com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards.TotalIntrusionsCard

@Preview
@Composable
fun DashboardScreen() {

    val context = LocalContext.current

    val dao = remember {
        AppDatabase.getDatabase(context).intruderLogDao()
    }

    val logs by dao.getAllLogs()
        .collectAsState(initial = emptyList())

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
            .padding(bottom = 80.dp)
    ) {

        Scaffold(

            containerColor = Color.Transparent,

            topBar = {

                DashboardTopBar()
            }

        ) { padding ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {

                    ProtectedCard()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            TotalIntrusionsCard(

                                total = IntrusionStats
                                    .getTotalIntrusions(logs)
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            LastIntrusionCard(

                                date = IntrusionStats
                                    .getLastIntrusionDate(logs),

                                time = IntrusionStats
                                    .getLastIntrusionTime(logs)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            ThisWeekCard(

                                count = IntrusionStats
                                    .getThisWeekIntrusions(logs)
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {

                            ThisMonthCard(

                                count = IntrusionStats
                                    .getThisMonthIntrusions(logs)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
    }
}