package com.wesign.wesign.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.wesign.wesign.domain.FirebaseAuthRepository
import com.wesign.wesign.domain.LoginResponse
import com.wesign.wesign.domain.OneTapSignInResponse
import com.wesign.wesign.domain.Response
import com.wesign.wesign.domain.SignInWithGoogleResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginState = MutableStateFlow<LoginResponse>(Response.Success(null))
    val loginState: StateFlow<LoginResponse> = _loginState.asStateFlow()

    private val _oneTapState = MutableStateFlow<OneTapSignInResponse>(Response.Success(null))
    val oneTapState: StateFlow<OneTapSignInResponse> = _oneTapState.asStateFlow()

    private val _signInWithGoogleState = MutableStateFlow<SignInWithGoogleResponse>(Response.Success(null))
    val signInWithGoogleState: StateFlow<SignInWithGoogleResponse> = _signInWithGoogleState.asStateFlow()


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

    fun setLoading(value: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = value
            )
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        firebaseAuthRepository.login(email, password).collectLatest { response ->
            _loginState.value = response
        }
    }

    fun oneTapSignIn() = viewModelScope.launch {
        firebaseAuthRepository.oneTapSignInWithGoogle().collectLatest {
            _oneTapState.value = it
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        firebaseAuthRepository.signInWithGoogle(googleCredential).collectLatest { 
            _signInWithGoogleState.value = it
        }
    }
}