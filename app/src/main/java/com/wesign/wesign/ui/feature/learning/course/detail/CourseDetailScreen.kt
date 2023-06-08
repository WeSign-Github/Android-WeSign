package com.wesign.wesign.ui.feature.learning.course.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.wesign.wesign.data.entity.CourseDetail
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun CourseDetailRoute(
    viewModel: CourseDetailViewModel = hiltViewModel(),
    idCourse: Int = -1,
    onNavigateBack: () -> Unit,
    onStartLearning: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCourse(idCourse)
    }

    CourseDetailScreen(uiState, onNavigateBack = onNavigateBack, onStartLearning = onStartLearning)
}

@Composable
fun CourseDetailScreen(
    uiState: CourseDetailState = CourseDetailState(),
    onNavigateBack: () -> Unit = {},
    onStartLearning: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                modifier = Modifier,
                title = "Course Detail",
                onNavigateBack = onNavigateBack
            )
        }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .padding(15.dp)
        ) {
            Text(
                uiState.course.title,
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .placeholder(
                        visible = uiState.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (uiState.isLoading) Spacer(modifier = Modifier.height(20.dp))

            Text(
                uiState.course.description,
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .placeholder(
                        visible = uiState.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) return@Box
                Button(
                    onClick = onStartLearning,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Mulai Belajar")
                }
            }
        }
    }
}


@Preview
@Composable
private fun CourseDetailScreenPreview() {
    WeSignTheme() {
        CourseDetailScreen(
            uiState = CourseDetailState(
                course = CourseDetail(
                    -1, "Alphabets 101", "Mempelajari alphabets A-Z dalam bahasda tangan", "", "",
                    emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun CourseDetailScreenPreviewLoading() {
    WeSignTheme() {
        CourseDetailScreen(
            uiState = CourseDetailState(
                isLoading = true,
                course = CourseDetail(
                    -1, "Alphabets 101", "Mempelajari alphabets A-Z dalam bahasda tangan", "", "",
                    emptyList()
                )
            )
        )
    }
}