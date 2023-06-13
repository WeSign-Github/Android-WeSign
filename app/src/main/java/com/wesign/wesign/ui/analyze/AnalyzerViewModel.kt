package com.wesign.wesign.ui.analyze

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.tensorflow.lite.task.vision.detector.Detection

class AnalyzerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyzerState())
    val uiState: StateFlow<AnalyzerState> = _uiState.asStateFlow()

    fun addHistory(value: Detection) {
        if (_uiState.value.detectionHistory.isNotEmpty()) {
            if (_uiState.value.detectionHistory.last().categories[0].label == value.categories[0].label) {
                return
            }
        }

        _uiState.update {
            it.copy(
                detectionHistory = it.detectionHistory.plus(value)
            )
        }

    }
}