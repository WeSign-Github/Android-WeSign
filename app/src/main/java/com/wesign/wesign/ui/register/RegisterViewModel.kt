package com.wesign.wesign.ui.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())

    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()


    fun setEmail(value: String) {
        _uiState.update {
            it.copy(
                email = value
            )
        }
    }

    fun setPassword(value: String) {
        _uiState.update {
            it.copy(
                password = value
            )
        }
    }

    fun setRePassword(value: String) {
        _uiState.update {
            it.copy(
                rePassword = value
            )
        }
    }

    fun togglePassword() {
        _uiState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    fun toggleRePassword() {
        _uiState.update {
            it.copy(
                isRePasswordVisible = !it.isRePasswordVisible
            )
        }
    }
}