package com.wesign.wesign.ui.analyze

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AnalyzerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyzerState())
    val uiState: StateFlow<AnalyzerState> = _uiState.asStateFlow()

    fun setState(uiState: AnalyzerState) {
        _uiState.update {
            uiState
        }
    }
}