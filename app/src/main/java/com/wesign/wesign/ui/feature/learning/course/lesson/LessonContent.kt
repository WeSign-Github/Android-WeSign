package com.wesign.wesign.ui.feature.learning.course.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.data.entity.LessonResponse

@Composable
fun LessonContent(
    modifier: Modifier = Modifier,
    lesson: LessonResponse.Data,
    onNextPressed: (Int?) -> Unit,
    onTryPressed: (Int, String) -> Unit,
    isCompleted: Boolean = false
) {
    var isImageLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                lesson.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                model = lesson.thumbnail,
                contentDescription = "",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(Color.DarkGray)
                    .placeholder(
                        isImageLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                onSuccess = {
                    isImageLoading = false
                },
                onLoading = {
                    isImageLoading = true
                }
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedButton(
                onClick = {
                    onTryPressed(lesson.id, lesson.title)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(text = "Try")
            }

            Spacer(modifier = Modifier.height(10.dp))

            ElevatedButton(
                onClick = {
                    onNextPressed(lesson.nextLessonId)
                },
                enabled = isCompleted,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                lesson.nextLessonId?.let {
                    Text(text = "Next")
                } ?: kotlin.run {
                    Text(text = "Finish")
                }
            }
        }

    }
}