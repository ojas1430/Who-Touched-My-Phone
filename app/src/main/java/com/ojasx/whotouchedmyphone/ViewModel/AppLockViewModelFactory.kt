package com.ojasx.whotouchedmyphone.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ojasx.whotouchedmyphone.AppLockLogic.AppLockManager

class AppLockViewModelFactory(
    private val manager: AppLockManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AppLockViewModel::class.java)) {
            return AppLockViewModel(manager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}