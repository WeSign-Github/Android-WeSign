package com.wesign.wesign.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.WeSignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    init {
        getUserProfile()
    }


    fun getUserProfile() {
        weSignRepository.getCurrentUser().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.result!!.data?.let {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = result.result.data
                            )
                        }
                    } ?: run {
                        _uiState.update {
                            it.copy(
                                isUserInfoEmpty = true
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    if (result.exception is SocketTimeoutException) {
                        getUserProfile()
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }

        }.launchIn(viewModelScope)
    }
}