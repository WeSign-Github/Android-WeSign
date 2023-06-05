package com.wesign.wesign.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.ui.theme.WeSignTheme

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onLogoutPressed: () -> Unit,
    onUserEmpty: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.isUserInfoEmpty) {
            onUserEmpty()
        }
    }

    ProfileScreen(
        onNavigateUp = onNavigateUp,
        onLogoutPressed = onLogoutPressed,
        uiState = uiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit = {},
    onLogoutPressed: () -> Unit = {},
    uiState: ProfileState = ProfileState(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Image(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {

                }
            )
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Image(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile Image",
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .placeholder(
                            visible = uiState.isLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                        ),
                )
                Text(
                    "${uiState.user?.firstName} ${uiState.user?.lastName}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.placeholder(
                        visible = uiState.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                )
                Text(
                    "(${uiState.user?.displayName})",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.placeholder(
                        visible = uiState.isLoading,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
                )
            }

            Column(
                Modifier
                    .weight(1.7f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 45.dp),
            ) {
                ProfileMenuItem(
                    imageVector = Icons.Filled.Settings,
                    color = Color.Black,
                    title = "Setting"
                )

                Divider(Modifier.fillMaxWidth())

                ProfileMenuItem(
                    imageVector = Icons.Filled.Logout,
                    color = Color.Red,
                    title = "Logout",
                    onClick = onLogoutPressed
                )

            }

        }
    }
}

@Composable
fun ProfileMenuItem(
    imageVector: ImageVector,
    color: Color,
    title: String,
    onClick: () -> Unit = {},
) {
    Row(
        Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector,
            contentDescription = title,
            Modifier
                .aspectRatio(1f)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(color)
        )
        Spacer(Modifier.width(40.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = color)
    }
}


@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        ProfileScreen()
    }
}