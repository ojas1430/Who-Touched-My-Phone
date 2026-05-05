package com.ojasx.whotouchedmyphone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.ojasx.whotouchedmyphone.Password.ConfirmPassword
import com.ojasx.whotouchedmyphone.Password.NewPassword
import com.ojasx.whotouchedmyphone.Screens.MainScreen
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val pinViewModel: PinViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "NewPassword"
    ) {

        composable("NewPassword") {
            NewPassword(pinViewModel,
                onPinSet = {
                    navController.navigate("ConfirmPassword")
                })
        }

        composable("ConfirmPassword") {
            ConfirmPassword(pinViewModel,
                onSuccess = {
                navController.navigate("MainScreen")
            })
        }

        composable("MainScreen"){
            MainScreen(navController)
        }
    }
}