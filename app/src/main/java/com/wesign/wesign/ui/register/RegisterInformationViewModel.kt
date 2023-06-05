package com.wesign.wesign.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesign.wesign.core.IDENTITY_PROVIDER
import com.wesign.wesign.data.entity.WeSignRegisterResponse
import com.wesign.wesign.data.entity.request.RegisterRequest
import com.wesign.wesign.domain.FirebaseAuthRepository
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.WeSignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterInformationViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterInformationState())
    val uiState: StateFlow<RegisterInformationState> = _uiState.asStateFlow()

    private val _registerState =
        MutableStateFlow<Resource<WeSignRegisterResponse>>(Resource.Success(null))
    val registerState: StateFlow<Resource<WeSignRegisterResponse>> = _registerState.asStateFlow()

    init {
        getFirebaseUserInfo()
    }

    private fun getFirebaseUserInfo() {
        val user = firebaseAuthRepository.currentUser!!
        _uiState.update {
            it.copy(
                displayName = user.displayName ?: ""
            )
        }

    }

    fun registerDetailInformation(firstName: String, lastName: String, displayName: String = "") {
        val request = RegisterRequest(
            providerId = firebaseAuthRepository.currentUser!!.uid,
            providerName = IDENTITY_PROVIDER,
            firstName = firstName,
            lastName = lastName,
            displayName = displayName,
            email = firebaseAuthRepository.currentUser!!.email.toString(),
            avatar = ""
        )

        weSignRepository.registerUser(request).onEach { result ->
            _registerState.update {
                result
            }
        }.launchIn(viewModelScope)
    }

    fun changeFirstName(value: String) {
        _uiState.update {
            it.copy(
                firstName = value
            )
        }
    }

    fun changeLastName(value: String) {
        _uiState.update {
            it.copy(
                lastName = value
            )
        }
    }

    fun changeDisplayName(value: String) {
        _uiState.update {
            it.copy(
                displayName = value
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
}
