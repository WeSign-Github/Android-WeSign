package com.wesign.wesign.ui.login

data class LoginUiState (
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
)