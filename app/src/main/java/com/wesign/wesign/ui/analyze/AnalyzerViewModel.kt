package com.wesign.wesign.ui.analyze

import androidx.lifecycle.ViewModel
import com.wesign.wesign.core.SignLanguageModels
import com.wesign.wesign.utils.ObjectDetectorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.tensorflow.lite.task.vision.detector.Detection
import javax.inject.Inject

@HiltViewModel
class AnalyzerViewModel @Inject constructor() : ViewModel() {

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

    fun setSignModel(model: SignLanguageModels) {
        _uiState.update {
            it.copy(
                selectedSignModel = model
            )
        }
    }

    fun setObjectDetectorHelper(helper: ObjectDetectorHelper?) {
        _uiState.update {
            it.copy(
                objectDetectorHelper = helper
            )
        }
    }
}