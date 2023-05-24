package com.wesign.wesign.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.R
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun HomeRoute(
    onAnalyzePressed: () -> Unit,
    onLearningPressed: () -> Unit,
    onProfilePressed: () -> Unit,
) {
    HomeScreen(
        onAnalyzePressed = onAnalyzePressed,
        onProfilePressed = onProfilePressed,
        onLearningPressed = onLearningPressed
    )
}

@Composable
fun HomeScreen(
    onAnalyzePressed: () -> Unit = {},
    onLearningPressed: () -> Unit = {},
    onProfilePressed: () -> Unit = {},
) {
    Scaffold() { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = 25.dp),
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            HomeTopBar(onProfilePressed)
            Text(
                "Your best sign language assistant",
                Modifier.padding(vertical = 35.dp),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                CardButton(
                    modifier = Modifier.weight(1f),
                    text = "Start Analyze",
                    imageResId = R.drawable.ic_analyze,
                    onClick = onAnalyzePressed
                )
                Spacer(modifier = Modifier.width(20.dp))
                CardButton(
                    modifier = Modifier.weight(1f),
                    text = "Start Learning",
                    imageResId = R.drawable.ic_learning,
                    onClick = onLearningPressed
                )
            }
        }

    }
}


@Composable
private fun CardButton(
    modifier: Modifier,
    text: String,
    imageResId: Int,
    onClick: () -> Unit = {},
) {
    Card(
        Modifier
            .then(modifier)
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(LocalExtendedColorScheme.current.surfaceContainer),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxSize(),
            Arrangement.SpaceBetween,
        ) {
            Image(
                painterResource(id = imageResId),
                contentDescription = text,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}


@Composable
private fun HomeTopBar(
    onProfilePressed: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(55.dp),
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Column() {
                Row() {
                    Text(
                        text = "Hello, ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Random Stranger!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = onProfilePressed,
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        LocalExtendedColorScheme.current.surfaceContainerLow,
                        shape = CircleShape
                    ),
            ) {
                Image(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                )
            }

        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    WeSignTheme {
        HomeScreen()
    }
}