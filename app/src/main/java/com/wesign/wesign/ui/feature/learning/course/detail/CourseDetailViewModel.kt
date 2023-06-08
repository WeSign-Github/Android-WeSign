package com.wesign.wesign.ui.feature.learning.course.detail

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
class CourseDetailViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(CourseDetailState())
    val uiState: StateFlow<CourseDetailState> = _uiState.asStateFlow()

    fun getCourse(id: Int) {
        weSignRepository.getDetailCourses(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, course = result.result!!.data)
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    if (result.exception is SocketTimeoutException) {
                        _uiState.update {
                            it.copy(isTryAgain = true)
                        }
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