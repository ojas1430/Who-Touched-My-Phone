package com.ojasx.whotouchedmyphone.Screens.Settings.Cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityQuestionScreen(
    navController: NavController
) {

    val questions = listOf(

        "What was your childhood nickname?",

        "What is the name of your first school?",

        "What was your first pet’s name?",

        "Which city were you born in?",

        "What is your favorite movie?",

        "What is your dream job?",

        "What was your first phone model?",

        "What is your mother’s maiden name?"
    )

    var selectedQuestionIndex by remember {
        mutableStateOf(-1)
    }

    var answer by remember {
        mutableStateOf("")
    }

    var showError by remember {
        mutableStateOf(false)
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
                        text = "Security Questions",
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
                modifier = Modifier.height(20.dp)
            )

            Text(

                text = "Choose a Security Question",

                style = MaterialTheme.typography.headlineMedium,

                color = Color.White,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(

                text =
                    "Select one question and set an answer for account recovery.",

                color = Color.LightGray
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            LazyColumn(

                modifier = Modifier.weight(1f),

                verticalArrangement = Arrangement.spacedBy(14.dp),

                contentPadding = PaddingValues(
                    bottom = 20.dp
                )
            ) {

                itemsIndexed(questions) { index, question ->

                    val isSelected =
                        selectedQuestionIndex == index

                    Box(

                        modifier = Modifier
                            .fillMaxWidth()
                            .background(

                                if (isSelected)
                                    Color(0xFF7B61FF).copy(alpha = 0.25f)
                                else
                                    Color(0xFF1A1F2E),

                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable {

                                selectedQuestionIndex = index
                                showError = false
                            }
                            .padding(18.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(

                                text = question,

                                color = Color.White,

                                modifier = Modifier.weight(1f)
                            )

                            if (isSelected) {

                                Icon(

                                    imageVector =
                                        Icons.Default.CheckCircle,

                                    contentDescription = null,

                                    tint = Color(0xFF7B61FF)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(

                value = answer,

                onValueChange = {

                    answer = it
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

                    text =
                        "Please select a question and enter an answer.",

                    color = Color.Red
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(

                onClick = {

                    if (
                        selectedQuestionIndex != -1 &&
                        answer.isNotBlank()
                    ) {

                        val selectedQuestion =
                            questions[selectedQuestionIndex]

                        // SAVE QUESTION + ANSWER HERE

                        navController.popBackStack()

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

                    text = "Save Security Question",

                    color = Color.White,

                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}