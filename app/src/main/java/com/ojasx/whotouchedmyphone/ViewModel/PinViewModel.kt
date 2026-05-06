package com.ojasx.whotouchedmyphone.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ojasx.whotouchedmyphone.RoomDb.PinRepository
import kotlinx.coroutines.launch

class PinViewModel(private val repository: PinRepository) : ViewModel() {

    private val _pin = mutableStateOf("")
    val pin get() = _pin

    private var firstPin: String? = null

    private val _error = mutableStateOf("")
    val error get() = _error

    private val _isFirstStepDone = mutableStateOf(false)
    val isFirstStepDone get() = _isFirstStepDone

    // Loading state to prevent multiple clicks
    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    fun onNumberClick(number: Int) {
        if (_pin.value.length < 4) {
            _pin.value += number.toString()
        }
    }

    fun onDeleteClick() {
        if (_pin.value.isNotEmpty()) {
            _pin.value = _pin.value.dropLast(1)
        }
    }

    fun onSetPinClick() {
        if (_pin.value.length != 4) {
            _error.value = "Enter 4 digit PIN"
            return
        }

        firstPin = _pin.value
        _pin.value = ""
        _isFirstStepDone.value = true
        _error.value = ""
    }

    fun onConfirmPinClick(onSuccess: () -> Unit) {
        if (_pin.value.length != 4) {
            _error.value = "Enter 4 digit PIN"
            return
        }

        if (_pin.value != firstPin) {
            _error.value = "PIN does not match"
            _pin.value = ""
            return
        }

        // Store in Room DB
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                repository.setPin(_pin.value)
                _pin.value = ""
                firstPin = null
                _isFirstStepDone.value = false
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to save PIN. Please try again."
                // Do not clear firstPin so user can retry confirmation
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetFirstStep() {
        _isFirstStepDone.value = false
        _pin.value = ""
        _error.value = ""
    }

    // Optional: Clear everything
    fun resetAll() {
        _pin.value = ""
        firstPin = null
        _isFirstStepDone.value = false
        _error.value = ""
    }
}

class PinViewModelFactory(private val repository: PinRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PinViewModel::class.java)) {
            return PinViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}