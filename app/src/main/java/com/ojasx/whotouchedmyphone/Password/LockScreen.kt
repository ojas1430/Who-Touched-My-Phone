package com.ojasx.whotouchedmyphone.Password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ojasx.whotouchedmyphone.CameraXManager
import com.ojasx.whotouchedmyphone.Password.NumberPad.NumberPad
import com.ojasx.whotouchedmyphone.Password.NumberPad.PinDots
import com.ojasx.whotouchedmyphone.R
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel

@Composable
fun LockScreen(
    pinViewModel: PinViewModel,
    cameraXManager: CameraXManager,
    lockedPackageName: String,
    onUnlockSuccess: () -> Unit
) {

    val pin = pinViewModel.pin.value

    var error by remember {
        mutableStateOf(false)
    }

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
    ) {

        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = (-40).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF7B4DFF).copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .blur(120.dp)
            )

            Box(
                modifier = Modifier
                    .offset(x = (-40).dp, y = (-20).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFE9DDFF).copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .blur(120.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.lockicon),
                    contentDescription = null,
                    modifier = Modifier
                        .shadow(20.dp, CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter PIN",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                PinDots(
                    pinLength = pin.length
                )

                if (error) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Wrong PIN",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        NumberPad(

            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),

            onNumberClick = {

                error = false

                pinViewModel.onNumberClick(it)
            },

            onDeleteClick = {

                pinViewModel.onDeleteClick()
            },

            onConfirmClick = {

                pinViewModel.verifyPin(

                    onSuccess = {

                        error = false

                        onUnlockSuccess()
                    },

                    onError = {

                        error = true

                        cameraXManager.takePhoto(

                            packageName = lockedPackageName,

                            onSuccess = {

                            },

                            onError = {

                            }
                        )
                    }
                )
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}