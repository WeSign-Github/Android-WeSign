package com.wesign.wesign.ui.texttosign

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesign.wesign.data.entity.TextToSignResponse
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
class TextToSignViewModel @Inject constructor(
    private val weSignRepository: WeSignRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TextToSignState())
    val uiState: StateFlow<TextToSignState> = _uiState.asStateFlow()

    init {
        getAllWord()
    }

    private fun getAllWord() {
        weSignRepository.getAllSignToTextWord().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            listWord = result.result
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    if (result.exception is SocketTimeoutException) {
                        _uiState.update {
                            it.copy(
                                isTryAgain = true
                            )
                        }
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

    fun appendWord(value: TextToSignResponse.SignWord) {
        _uiState.update {
            it.copy(
                selectedWord = it.selectedWord.plus(value)
            )
        }
        Log.d("TextToSignViewModel", uiState.value.selectedWord.toString())
    }

    fun removeWord(value: TextToSignResponse.SignWord) {
        _uiState.update {
            it.copy(
                selectedWord = it.selectedWord.minus(value)
            )
        }
    }

    fun tryAgain() {
        _uiState.update {
            it.copy(
                isTryAgain = false
            )
        }
        getAllWord()
    }
}