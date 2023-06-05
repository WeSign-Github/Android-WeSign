package com.wesign.wesign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.WeSignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository
) : ViewModel() {



    fun getCurrentUserProfile() {
        weSignRepository.getCurrentUser().onEach {
            when (it) {
                is Resource.Success -> {
                }

                else -> {

                }
            }

        }.launchIn(viewModelScope)
    }
}