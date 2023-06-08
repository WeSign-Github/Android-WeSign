package com.wesign.wesign.ui.feature.learning.course.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun LessonDrawerContent() {
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
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListLessonScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonDrawerContent()
    }
}