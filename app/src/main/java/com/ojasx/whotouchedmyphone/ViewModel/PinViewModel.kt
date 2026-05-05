package com.ojasx.whotouchedmyphone.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PinViewModel : ViewModel() {

    private val _pin = mutableStateOf("")
    val pin get() = _pin

    private var firstPin: String? = null

    private val _error = mutableStateOf("")
    val error get() = _error

    private val _isFirstStepDone = mutableStateOf(false)
    val isFirstStepDone get() = _isFirstStepDone

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

        if (_pin.value == firstPin) {
            _error.value = ""
            _pin.value = ""
            firstPin = null
            _isFirstStepDone.value = false
            onSuccess()
        } else {
            _error.value = "PIN does not match"
            _pin.value = ""
        }
    }

    fun resetFirstStep() {
        _isFirstStepDone.value = false
    }
}