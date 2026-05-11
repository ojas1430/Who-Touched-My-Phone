package com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ojasx.whotouchedmyphone.R

@Composable
fun TotalIntrusionsCard(
    total: Int
) {

    GlassCard {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart),

                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Total Intrusions",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = total.toString(),
                    color = Color.White,
                    fontSize = 26.sp
                )
            }

            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
                    .offset(y = 10.dp)
            )
        }
    }
}