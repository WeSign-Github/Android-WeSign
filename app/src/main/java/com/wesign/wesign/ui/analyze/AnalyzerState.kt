package com.wesign.wesign.ui.analyze

import androidx.compose.ui.unit.IntSize
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

data class AnalyzerState(
    val handLandmarkerResult: HandLandmarkerResult? = null,
    val landmarkImageSize: IntSize = IntSize(0, 0),
    val runningMode: RunningMode = RunningMode.LIVE_STREAM
)
