package com.ojasx.whotouchedmyphone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ojasx.whotouchedmyphone.Password.ConfirmPassword
import com.ojasx.whotouchedmyphone.Password.NewPassword
import com.ojasx.whotouchedmyphone.RoomDb.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PinRepository
import com.ojasx.whotouchedmyphone.Screens.MainScreen
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModel
import com.ojasx.whotouchedmyphone.ViewModel.PinViewModelFactory

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // Create Database → Repository → Factory → ViewModel
    val database = AppDatabase.getDatabase(context)
    val repository = PinRepository(database.pinDao())
    val factory = PinViewModelFactory(repository)

    val pinViewModel: PinViewModel = viewModel(
        factory = factory
    )

    NavHost(
        navController = navController,
        startDestination = "NewPassword"
    ) {

        composable("NewPassword") {
            NewPassword(
                pinViewModel = pinViewModel,
                onNext = {
                    navController.navigate("ConfirmPassword")
                }
            )
        }

        composable("ConfirmPassword") {
            ConfirmPassword(
                pinViewModel = pinViewModel,
                onSuccess = {
                    navController.navigate("MainScreen") {
                        popUpTo("NewPassword") { inclusive = true }

                    }
                },
                onMismatch = {
                    navController.popBackStack()
                }
            )
        }

        composable("MainScreen") {
            MainScreen(navController)
        }
    }
}