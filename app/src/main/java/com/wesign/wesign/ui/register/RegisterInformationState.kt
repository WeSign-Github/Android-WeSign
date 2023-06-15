package com.wesign.wesign.ui.register

data class RegisterInformationState(
    val firstName: String = "",
    val lastName: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false
)
