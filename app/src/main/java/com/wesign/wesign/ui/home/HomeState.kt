package com.wesign.wesign.ui.home

import com.wesign.wesign.data.entity.SelfUserResponse

data class HomeState(
    val profile: SelfUserResponse.User? = null,
    val isLoading: Boolean = false,
    val isProfileEmpty: Boolean = false
)
