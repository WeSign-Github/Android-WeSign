package com.wesign.wesign.component
// COMMENTED because MediaPipe is not Used
// TODO: Implement it Later
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.PointMode
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.layout.onGloballyPositioned
//import com.google.mediapipe.tasks.vision.core.RunningMode
//import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
//import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
//import kotlin.math.max
//import kotlin.math.min
//
//
//private const val LANDMARK_STROKE_WIDTH = 8F
//
//@Composable
//fun HandOverlayView(
//    modifier: Modifier = Modifier,
//    handLandmarkerResults: HandLandmarkerResult? = null,
//    imageHeight: Int = 1,
//    imageWidth: Int = 1,
//    runningMode: RunningMode = RunningMode.IMAGE
//) {
//
//    var width by remember {
//        mutableStateOf(0f)
//    }
//    var height by remember {
//        mutableStateOf(0f)
//    }
//
//    var scaleFactor = 1f
//
//    LaunchedEffect(imageWidth, imageHeight) {
//        scaleFactor = when (runningMode) {
//            RunningMode.IMAGE,
//            RunningMode.VIDEO -> {
//                min(width * 1f / imageWidth, height * 1f / imageHeight)
//            }
//
//            RunningMode.LIVE_STREAM -> {
//                max(width * 1f / imageWidth, height * 1f / imageHeight)
//            }
//        }
//    }
//
//    Canvas(
//        modifier = modifier
//            .onGloballyPositioned {
//                height = it.size.height.toFloat()
//                width = it.size.width.toFloat()
//            },
//    ) {
//        handLandmarkerResults?.let { handLandmarkerResult ->
//            for (landmark in handLandmarkerResult.landmarks()) {
//                for (normalizedLandmark in landmark) {
//                    drawPoints(
//                        listOf(
//                            Offset(
//                                normalizedLandmark.x() * imageWidth * scaleFactor,
//                                normalizedLandmark.y() * imageHeight * scaleFactor
//                            ),
//                        ),
//                        pointMode = PointMode.Points,
//                        color = Color.Yellow,
//                        strokeWidth = LANDMARK_STROKE_WIDTH,
//                        cap = StrokeCap.Square,
//                    )
//                }
//
//                HandLandmarker.HAND_CONNECTIONS.forEach {
//                    drawLine(
//                        color = Color.Cyan,
//                        start = Offset(
//                            handLandmarkerResult.landmarks()[0][it!!.start()].x() * imageWidth * scaleFactor,
//                            handLandmarkerResult.landmarks()[0][it.start()].y() * imageHeight * scaleFactor
//                        ),
//                        end = Offset(
//                            handLandmarkerResult.landmarks()[0][it.end()].x() * imageWidth * scaleFactor,
//                            handLandmarkerResult.landmarks()[0][it.end()].y() * imageHeight * scaleFactor
//                        ),
//                        strokeWidth = LANDMARK_STROKE_WIDTH,
//                        cap = StrokeCap.Round,
//                    )
//                }
//            }
//        }
//    }
//}