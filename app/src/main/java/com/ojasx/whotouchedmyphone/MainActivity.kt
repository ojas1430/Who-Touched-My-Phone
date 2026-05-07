package com.ojasx.whotouchedmyphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.ojasx.whotouchedmyphone.RoomDb.AppDatabase
import com.ojasx.whotouchedmyphone.RoomDb.PinRepository
import com.ojasx.whotouchedmyphone.navigation.AppNavigation
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).pinDao()
        val repository = PinRepository(dao)

        lifecycleScope.launch {

            val isPinSet = repository.isPinSet()

            setContent {

                AppNavigation(isPinSet = isPinSet)
            }
        }
    }
}