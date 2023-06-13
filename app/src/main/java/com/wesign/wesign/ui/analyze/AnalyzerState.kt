package com.wesign.wesign.ui.analyze

import androidx.camera.core.CameraSelector
import com.wesign.wesign.utils.ObjectDetectorHelper
import org.tensorflow.lite.task.vision.detector.Detection

data class AnalyzerState(
    val cameraLensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val detectionHistory: List<Detection> = listOf(),
    val objectDetectorHelper: ObjectDetectorHelper? = null,
)
