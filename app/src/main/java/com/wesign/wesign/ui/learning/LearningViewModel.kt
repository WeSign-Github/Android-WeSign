package com.wesign.wesign.ui.learning

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
import javax.inject.Inject

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(LearningState())
    val uiState: StateFlow<LearningState> = _uiState.asStateFlow()

    init {
        getCourses()
    }

    fun getCourses() {
        weSignRepository.getCourses().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, courseList = result.result!!.data)
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}