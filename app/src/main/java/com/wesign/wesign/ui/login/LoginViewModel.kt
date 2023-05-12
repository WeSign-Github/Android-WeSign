package com.wesign.wesign.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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

    fun togglePasswordVisible() {
        _uiState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    fun login() {

    }
}