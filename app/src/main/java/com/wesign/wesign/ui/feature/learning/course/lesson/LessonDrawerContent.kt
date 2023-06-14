package com.wesign.wesign.ui.feature.learning.course.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.data.entity.Lesson
import com.wesign.wesign.data.entity.LessonResponse
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun LessonDrawerContent(
    listLesson: List<Lesson> = listOf(),
    currentLesson: LessonResponse.Data,
    progressPercent: Int = 0,
    onLessonClicked: (Lesson) -> Unit = {},
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            Modifier.fillMaxSize()
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Lesson List",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "101 Alphabet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                LinearProgressIndicator(
                    progress = 0.5f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "20%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            LazyColumn(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(listLesson) {
                    NavigationDrawerItem(
                        selected = currentLesson.id == it.id,
                        modifier = Modifier.padding(vertical = 10.dp),
                        label = {
                            Row(
                                verticalAlignment = CenterVertically,
                            ) {
                                if (it.completed) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = "Lesson Completed",
                                        tint = Color.Green
                                    )

                                } else {
                                    Icon(
                                        Icons.Filled.Circle,
                                        modifier = Modifier
                                            .height(15.dp)
                                            .aspectRatio(1f),
                                        contentDescription = "Lesson Not Completed"
                                    )
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(it.title)
                            }
                        },
                        onClick = {
                            onLessonClicked(it)
                        },
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListLessonScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonDrawerContent(
            listLesson = listOf(
                Lesson(1, 1, "A", "", true, "", ""),
                Lesson(2, 1, "B", "", false, "", ""),
                Lesson(3, 1, "C", "", false, "", ""),
                Lesson(4, 1, "D", "", false, "", ""),
            ),
            currentLesson = LessonResponse.Data(3, 1, "C", "", "false", "", 0),
        )
    }
}