package com.wesign.wesign.ui.feature.learning.learning.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun CourseListItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onItemClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Box(
                Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = title,
                    modifier = modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    content,
                    modifier = modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = onItemClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Mulai")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CourseListItemPreview() {
    WeSignTheme() {
        CourseListItem(
            Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(10.dp),
            title = "Course Alphabet 101",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"
        )
    }
}
