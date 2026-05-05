package com.ojasx.whotouchedmyphone.Password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.Password.NumberPad.NumberPad
import com.ojasx.whotouchedmyphone.Password.NumberPad.PinDots
import com.ojasx.whotouchedmyphone.R
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel

@Composable
fun NewPassword(
    pinViewModel: PinViewModel,
    onPinSet:()->Unit
) {
    val isDone = pinViewModel.isFirstStepDone.value
    val bgBrush = remember {
        Brush.verticalGradient(
            listOf(
                Color(0xFF0B0E17),
                Color(0xFF111528),
                Color(0xFF0B0E17)
            )
        )
    }

    LaunchedEffect(isDone) {
        if (isDone) {
            onPinSet()
        }
    }

        val pin = pinViewModel.pin.value
        val error = pinViewModel.error.value

        Column(
            modifier = Modifier.fillMaxSize()
                .background(bgBrush)
        ) {

        // Top content (60%)
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.firstlock),
                    contentDescription = null
                )
                Text(
                    "Secure Your Phone",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold

                )
                Spacer(Modifier.height(20.dp))
                Text(
                    "Set a 4 digit PIN to protect your phone and catch intruders.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center   ,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
                Spacer(Modifier.height(20.dp))
                PinDots(
                    pinLength = pin.length
                )
                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = Color.Red
                    )
                }
            }
        }

        // Bottom NumberPad (40%)
        NumberPad(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
            onNumberClick = { number ->
                pinViewModel.onNumberClick(number)
            },

            onDeleteClick = {
                pinViewModel.onDeleteClick()
            },

            onConfirmClick = {
                pinViewModel.onSetPinClick()
            }
        )
        Spacer(Modifier.height(20.dp))
    }
}

