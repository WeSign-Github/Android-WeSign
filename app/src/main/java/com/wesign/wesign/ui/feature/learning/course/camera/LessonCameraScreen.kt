package com.wesign.wesign.ui.feature.learning.course.camera

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesign.wesign.component.MLCameraView
import com.wesign.wesign.core.SignLanguageModels
import com.wesign.wesign.ui.feature.learning.course.camera.common.LessonCameraTopBar
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailState
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailViewModel
import com.wesign.wesign.ui.feature.learning.course.lesson.LessonState
import com.wesign.wesign.ui.feature.learning.course.lesson.LessonViewModel
import com.wesign.wesign.ui.theme.WeSignTheme
import com.wesign.wesign.utils.ObjectDetectorHelper
import org.tensorflow.lite.task.vision.detector.Detection



// TODO Lesson Completed state is not resetting after new lesson opened
@Composable
fun LessonCameraRoute(
    courseDetailViewModel: CourseDetailViewModel = hiltViewModel(),
    lessonViewModel: LessonViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val courseDetailState by courseDetailViewModel.uiState.collectAsStateWithLifecycle()
    val lessonState by lessonViewModel.uiState.collectAsStateWithLifecycle()

    LessonCameraScreen(
        lessonState = lessonState,
        courseDetailState = courseDetailState,
        onNavigateBack = onNavigateBack,
        onChallengeCorrect = {
            lessonViewModel.completeCurrentLesson(lessonState.lesson.id)
        },
    )
}

@Composable
fun LessonCameraScreen(
    lessonState: LessonState = LessonState(),
    courseDetailState: CourseDetailState = CourseDetailState(),
    onNavigateBack: () -> Unit = {},
    onCompleteLesson: () -> Unit = {},
    onChallengeCorrect: () -> Unit = {},
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {

        }
    ) { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            MLCameraView(
                modifier = Modifier.fillMaxSize(),
                objectDetectorHelper = ObjectDetectorHelper(
                    context = context,
                    signModel = if (courseDetailState.course.language == "sibi") SignLanguageModels.SIBI else SignLanguageModels.BISINDO,
                    objectDetectorListener = object : ObjectDetectorHelper.DetectorListener {
                        override fun onError(error: String) {
                        }

                        override fun onResults(
                            results: MutableList<Detection>?,
                            inferenceTime: Long,
                            imageHeight: Int,
                            imageWidth: Int
                        ) {
                            Log.d("LessonCamera", "$inferenceTime")
                            results?.let {
                                if (results.size <= 0) {
                                    return@let
                                }

                                for (detect in results) {
                                    Log.d("LessonCamera", detect.categories[0].label)
                                    if (detect.categories[0].label.lowercase() == lessonState.lesson.title.lowercase()) {
                                        if (!lessonState.isChallengeComplete) {
                                            onChallengeCorrect()
                                        }
                                    }
                                }
                            }
                        }
                    }
                ),
            )

            LessonCameraTopBar(
                onNavigateUp = onNavigateBack,
                onSwitchCamera = {

                },
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        if (lessonState.isChallengeComplete)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                    .padding(10.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Question Card",
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = lessonState.lesson.title,
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                if (lessonState.isChallengeComplete) {
                    ElevatedButton(
                        onClick = onNavigateBack, colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Complete Lesson")
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCameraScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonCameraScreen()
    }
}