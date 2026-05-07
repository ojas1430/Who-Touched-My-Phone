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
fun AppNavigation(isPinSet: Boolean) {

    val navController = rememberNavController()
    val context = LocalContext.current

    val database = AppDatabase.getDatabase(context)
    val repository = PinRepository(database.pinDao())
    val factory = PinViewModelFactory(repository)

    val pinViewModel: PinViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = if (isPinSet) "home" else "NewPassword"
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
                    navController.navigate("home") {
                        popUpTo("NewPassword") { inclusive = true }
                    }
                },
                onMismatch = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            MainScreen(navController)
        }
    }
}