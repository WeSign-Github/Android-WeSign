package com.wesign.wesign.ui.feature.learning.learning

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.data.entity.Course
import com.wesign.wesign.ui.feature.learning.learning.component.CourseListItem
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
fun LearningRoute(
    viewModel: LearningViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onItemClicked: (Int) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LearningScreen(
        onNavigateUp = onNavigateUp,
        onTryAgainPressed = viewModel::tryAgain,
        onItemClicked = onItemClicked,
        uiState = uiState
    )
}

@Composable
fun LearningScreen(
    onNavigateUp: () -> Unit = {},
    onTryAgainPressed: () -> Unit = {},
    onItemClicked: (Int) -> Unit = {},
    uiState: LearningState = LearningState()
) {
    val lazyState = rememberLazyListState()

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Learning",
                onNavigateBack = onNavigateUp
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            if (uiState.isTryAgain) {
                TryAgain(onButtonClick = onTryAgainPressed)
            }

            Text("Course", style = MaterialTheme.typography.titleLarge)

            if (uiState.isLoading) {
                (0..3).forEach { _ ->
                    CourseListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .padding(vertical = 10.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                        title = "",
                        content = ""
                    )
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    lazyState,
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    items(uiState.courseList) {
                        CourseListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .padding(vertical = 10.dp),
                            title = it.title,
                            content = it.description,
                            onItemClick = { onItemClicked(it.id) }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TryAgain(onButtonClick: () -> Unit = {}) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Try Again")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearningScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LearningScreen()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearningScreenPreviewLoading() {
    WeSignTheme(dynamicColor = false) {
        LearningScreen(uiState = LearningState(isLoading = true))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearningScreenPreviewTryAgain() {
    WeSignTheme(dynamicColor = false) {
        LearningScreen(uiState = LearningState(isTryAgain = true))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearningScreenPreviewWithCourse() {
    WeSignTheme(dynamicColor = false) {
        LearningScreen(
            uiState = LearningState(
                courseList = listOf(
                    Course(1, "Alphabet 101", "Lorem Ipsum", "", "",""),
                    Course(2, "Alphabet 101", "Lorem Ipsum", "", "",""),
                    Course(3, "Alphabet 101", "Lorem Ipsum", "", "",""),
                    Course(4, "Alphabet 101", "Lorem Ipsum", "", "",""),
                )
            )
        )
    }
}