package com.wesign.wesign.ui.feature.learning.course.camera.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun LessonCameraTopBar(
    onNavigateUp: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Row(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(45.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        IconButton(
            onClick = onNavigateUp,
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    Color.Black.copy(
                        alpha = 0.65f
                    ),
                    shape = CircleShape
                ),
        ) {
            Image(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onSwitchCamera,
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    Color.Black.copy(
                        alpha = 0.65f
                    ),
                    shape = CircleShape
                ),
        ) {
            Image(
                Icons.Filled.Cameraswitch,
                contentDescription = "Back",
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}


@Preview
@Composable
private fun LessonCameraTopBarPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonCameraTopBar(
            onNavigateUp = {

            },
            onSwitchCamera = {

            },
        )
    }
}