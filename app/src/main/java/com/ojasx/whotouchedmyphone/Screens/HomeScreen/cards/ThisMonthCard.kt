package com.ojasx.whotouchedmyphone.Screens.HomeScreen.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ojasx.whotouchedmyphone.R

@Preview
@Preview
@Composable
fun ThisMonthCard() {
    GlassCard {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "This Month",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "18",
                    color = Color.White,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )
            }
        }
    }
}