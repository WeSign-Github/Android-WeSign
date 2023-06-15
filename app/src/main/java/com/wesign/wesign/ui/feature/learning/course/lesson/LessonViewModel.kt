package com.wesign.wesign.ui.feature.learning.course.lesson

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
class LessonViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(LessonState())
    val uiState: StateFlow<LessonState> = _uiState.asStateFlow()


    fun getLesson(lessonId: Int) {
        weSignRepository.getLesson(lessonId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            lesson = result.result!!.data
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
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

    fun completeCurrentLesson(lessonCurrentId: Int, nextLessonId: Int) {
        weSignRepository.completeLesson(lessonCurrentId).onEach {
            when (it) {
                is Resource.Success -> {
                    nextLesson(nextLessonId)
                    _uiState.update {
                        it.copy(
                            isChallengeComplete = true
                        )
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    fun completeCurrentLesson(lessonCurrentId: Int) {
        weSignRepository.completeLesson(lessonCurrentId).onEach {result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isChallengeComplete = true
                        )
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }


    fun nextLesson(nextLessonId: Int) {
        getLesson(nextLessonId)
    }
}