package com.ojasx.whotouchedmyphone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ojasx.whotouchedmyphone.AppLockLogic.AppLockManager
import com.ojasx.whotouchedmyphone.ViewModel.AppLockViewModelFactory
import com.ojasx.whotouchedmyphone.Password.ConfirmPassword
import com.ojasx.whotouchedmyphone.Password.NewPassword
import com.ojasx.whotouchedmyphone.Permissions.PermissionSetupScreen
import com.ojasx.whotouchedmyphone.Permissions.isAccessibilityServiceEnabled
import com.ojasx.whotouchedmyphone.Permissions.isIgnoringBatteryOptimizations
import com.ojasx.whotouchedmyphone.Permissions.isOverlayPermissionGranted
import com.ojasx.whotouchedmyphone.RoomDb.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PinRepository
import com.ojasx.whotouchedmyphone.Screens.MainScreen
import com.ojasx.whotouchedmyphone.ViewModel.AppLockViewModel
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
    val appLockManager = AppLockManager(context)

    val appLockFactory = AppLockViewModelFactory(appLockManager)

    val appLockViewModel: AppLockViewModel =
        viewModel(factory = appLockFactory)

    val allPermissionsGranted =
        isOverlayPermissionGranted(context) &&
                isIgnoringBatteryOptimizations(context) &&
                isAccessibilityServiceEnabled(context)

    val startDestination = when {

        !allPermissionsGranted -> "permissions"

        isPinSet -> "home"

        else -> "NewPassword"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("permissions") {

            PermissionSetupScreen(

                onAllPermissionsGranted = {

                    if (isPinSet) {

                        navController.navigate("home") {
                            popUpTo("permissions") {
                                inclusive = true
                            }
                        }

                    } else {

                        navController.navigate("NewPassword") {
                            popUpTo("permissions") {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

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

                        popUpTo("NewPassword") {
                            inclusive = true
                        }
                    }
                },

                onMismatch = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {

            MainScreen(
                appLockViewModel,
                pinViewModel,
                navController
            )
        }
    }
}