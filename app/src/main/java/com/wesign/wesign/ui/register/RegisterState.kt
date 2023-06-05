package com.wesign.wesign.ui.register

import com.google.firebase.auth.FirebaseUser

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRePasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val firebaseUser: FirebaseUser? = null
)
