package com.wesign.wesign.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val EMAIL_KEY = "email"
private const val PASSWORD_KEY = "password"

class LoginViewModel constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val email = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    val password = savedStateHandle.getStateFlow(PASSWORD_KEY, "")


    fun login() {

    }
}