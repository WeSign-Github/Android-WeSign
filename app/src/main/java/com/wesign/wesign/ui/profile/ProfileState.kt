package com.wesign.wesign.ui.profile

import com.wesign.wesign.data.entity.SelfUserResponse

data class ProfileState(
    val isLoading: Boolean = false,
    val isUserInfoEmpty: Boolean = false,
    val user: SelfUserResponse.User? = null,
)
