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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.R
import com.wesign.wesign.data.entity.SelfUserResponse
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onAnalyzePressed: () -> Unit,
    onLearningPressed: () -> Unit,
    onProfilePressed: () -> Unit,
    onTextToSignPressed: () -> Unit,
    onProfileEmpty: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState.isProfileEmpty) {
            onProfileEmpty()
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            if (uiState.profile == null) {
                viewModel.getProfile()
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onAnalyzePressed = onAnalyzePressed,
        onProfilePressed = onProfilePressed,
        onLearningPressed = onLearningPressed,
        onTextToSignPressed = onTextToSignPressed
    )
}

@Composable
fun HomeScreen(
    uiState: HomeState = HomeState(),
    onAnalyzePressed: () -> Unit = {},
    onLearningPressed: () -> Unit = {},
    onProfilePressed: () -> Unit = {},
    onTextToSignPressed: () -> Unit = {}
) {
    Scaffold() { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(horizontal = 25.dp),
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            HomeTopBar(
                onProfilePressed,
                isLoading = uiState.isLoading,
                profile = uiState.profile
            )
            Text(
                "Your best sign language assistant",
                Modifier.padding(vertical = 35.dp),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                CardButton(
                    modifier = Modifier.weight(1f),
                    text = "Text to Sign",
                    imageResId = R.drawable.ic_shorttext,
                    onClick = onTextToSignPressed
                )
                Spacer(modifier = Modifier.width(20.dp))
                CardButton(
                    modifier = Modifier.weight(1f),
                    text = "Start Learning",
                    imageResId = R.drawable.ic_learning,
                    onClick = onLearningPressed
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LongCardButton(
                text = "Start Analyze",
                imageResId = R.drawable.ic_analyze,
                onClick = onAnalyzePressed
            )
        }

    }
}

@Composable
private fun LongCardButton(
    modifier: Modifier = Modifier,
    imageResId: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Card(
        Modifier
            .then(modifier)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(LocalExtendedColorScheme.current.surfaceContainer),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = imageResId),
                contentDescription = text,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
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
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp),
                contentDescription = text,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}


@Composable
private fun HomeTopBar(
    onProfilePressed: () -> Unit,
    profile: SelfUserResponse.User?,
    isLoading: Boolean
) {

    var isImageLoading by remember {
        mutableStateOf(false)
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(55.dp),
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Column() {
                Row(
                    Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                ) {
                    Text(
                        text = "Hello, ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${profile?.displayName}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Text(
                    text = "Welcome Back",
                    Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    ).padding(vertical = if(isLoading) 3.dp else 0.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
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
                    )
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                        shape = CircleShape
                    ),
            ) {
                profile?.let {
                    if (it.avatar.isNotEmpty()) {
                        AsyncImage(
                            model = it.avatar,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                                .clip(CircleShape),
                            onLoading = {
                                isImageLoading = true
                            },
                            onSuccess = {
                                isImageLoading = false
                            }
                        )
                    } else {
                        Image(
                            Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            Modifier
                                .padding(8.dp)
                                .fillMaxSize()
                        )
                    }
                } ?: run {
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
}


@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        HomeScreen()
    }
}