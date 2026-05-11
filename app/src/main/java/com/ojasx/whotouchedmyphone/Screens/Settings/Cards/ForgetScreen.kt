package com.ojasx.whotouchedmyphone.Screens.Settings.Cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ojasx.whotouchedmyphone.RoomDb.PIN.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPinScreen(
    navController: NavController,
    onAnswerCorrect: () -> Unit
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var savedQuestion by remember {
        mutableStateOf("")
    }

    var savedAnswer by remember {
        mutableStateOf("")
    }

    var enteredAnswer by remember {
        mutableStateOf("")
    }

    var showError by remember {
        mutableStateOf(false)
    }

    // Load saved question + answer
    LaunchedEffect(Unit) {

        scope.launch {

            val data =
                AppDatabase
                    .getDatabase(context)
                    .securityAnswerDao()
                    .getSecurityData()

            if (data != null) {

                savedQuestion = data.question
                savedAnswer = data.answer
            }
        }
    }

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
                        text = "Forgot PIN",
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
        ) {

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Text(

                text = "Recover Your PIN",

                style = MaterialTheme.typography.headlineMedium,

                color = Color.White,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(

                text =
                    "Answer your security question to reset your PIN.",

                color = Color.LightGray
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            // Question Card
            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF1A1F2E),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {

                Column {

                    Text(

                        text = "Security Question",

                        color = Color(0xFFB388FF),

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(

                        text = savedQuestion,

                        color = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(

                value = enteredAnswer,

                onValueChange = {

                    enteredAnswer = it
                    showError = false
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {

                    Text(
                        text = "Enter your answer",
                        color = Color.LightGray
                    )
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                visualTransformation =
                    PasswordVisualTransformation(),

                colors = OutlinedTextFieldDefaults.colors(

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedBorderColor = Color(0xFF7B61FF),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color.White
                ),

                shape = RoundedCornerShape(16.dp),

                singleLine = true
            )

            if (showError) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(

                    text = "Incorrect answer",

                    color = Color.Red
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Button(

                onClick = {

                    if (

                        enteredAnswer.trim()
                            .equals(
                                savedAnswer.trim(),
                                ignoreCase = true
                            )
                    ) {

                        onAnswerCorrect()

                    } else {

                        showError = true
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B61FF)
                )
            ) {

                Text(

                    text = "Verify Answer",

                    color = Color.White,

                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}