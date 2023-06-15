package com.wesign.wesign.ui.feature.learning.course.lesson

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailState
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailViewModel
import com.wesign.wesign.ui.theme.WeSignTheme
import kotlinx.coroutines.launch


@Composable
fun LessonRoute(
    courseDetailViewModel: CourseDetailViewModel = hiltViewModel(),
    viewModel: LessonViewModel = hiltViewModel(),
    lessonId: Int,
    onCourseCompletePressed: () -> Unit,
    onNavigateBack: () -> Unit,
    onTryPressed: (String) -> Unit,
) {

    val sharedUiState by courseDetailViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getLesson(lessonId)
        courseDetailViewModel.getPercentageProgress()
    }

    LessonScreen(
        sharedState = sharedUiState,
        uiState = uiState,
        lessonId = lessonId,
        onNavigateBack = onNavigateBack,
        onChangeLesson = { targetLessonId ->
            targetLessonId?.let {
                viewModel.getLesson(it)
            } ?: run {
                onCourseCompletePressed()
            }
        },
        onTryPressed = onTryPressed,
    )
}

@Composable
fun LessonScreen(
    sharedState: CourseDetailState = CourseDetailState(),
    uiState: LessonState = LessonState(),
    lessonId: Int = -1,
    onNavigateBack: () -> Unit = {},
    onChangeLesson: (Int?) -> Unit = {},
    onTryPressed: (String) -> Unit = {},
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        LessonDrawerContent(
                            listLesson = sharedState.course.lessons,
                            currentLesson = uiState.lesson,
                            courseName = sharedState.course.title,
                            progressPercent = sharedState.progressPercentage,
                            onLessonClicked = { selectedLesson ->
                                if (drawerState.isOpen) scope.launch { drawerState.close() }
                                onChangeLesson(selectedLesson.id)
                            }
                        )
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = "Text to Sign",
                            onNavigateBack = onNavigateBack,
                            actions = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.List,
                                        contentDescription = "Lesson List"
                                    )
                                }
                            }
                        )
                    },
                ) { contentPadding ->

                    if (uiState.isLoading) {
                        LessonLoading()
                    } else {
                        LessonContent(
                            Modifier
                                .padding(contentPadding)
                                .padding(20.dp),
                            lesson = uiState.lesson,
                            onNextPressed = onChangeLesson,
                            onTryPressed = { id, challenge ->
                                onTryPressed(challenge)
                            },
                            isCompleted = true
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun LessonLoading() {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Loading",
                modifier = Modifier
                    .width(100.dp)
                    .placeholder(true, highlight = PlaceholderHighlight.shimmer()),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(Color.DarkGray)
                    .placeholder(
                        true,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
            )
        }

    }
}

@Preview
@Composable
private fun LessonScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonScreen()
    }
}

@Preview
@Composable
private fun LessonScreenLoadingPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonScreen(uiState = LessonState(isLoading = true))
    }
}