package com.wesign.wesign.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesign.wesign.domain.FirebaseAuthRepository
import com.wesign.wesign.domain.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

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

    fun register(email: String, password: String) {
        Log.d("RegisterViewModel", "Start Registering...")
        firebaseAuthRepository.register(email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("RegisterViewModel", "SUCCESS...")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            firebaseUser = result.result
                        )
                    }

                }

                is Resource.Loading -> {
                    Log.d("RegisterViewModel", "LOADING...")
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Error -> {
                    Log.d("RegisterViewModel", "ERROR...")
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

            }
        }.launchIn(viewModelScope)
    }
}