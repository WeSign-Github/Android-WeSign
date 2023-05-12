package com.wesign.wesign.ui.register

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRePasswordVisible: Boolean = false,
)
