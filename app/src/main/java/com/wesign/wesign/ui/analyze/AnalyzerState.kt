package com.wesign.wesign.ui.analyze

import androidx.camera.core.CameraSelector

data class AnalyzerState(
    val cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK
)
