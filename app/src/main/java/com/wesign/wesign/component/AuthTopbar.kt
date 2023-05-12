package com.wesign.wesign.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme

@Composable
fun AuthTopbar(
    title: String,
    modifier: Modifier = Modifier,
    showNavigateUp: Boolean = false,
    onNavigateUp: () -> Unit = { }
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(185.dp)
            .background(LocalExtendedColorScheme.current.surfaceContainer)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxSize()) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)) {
                if (showNavigateUp) {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Image(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

            }

            Box(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 33.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AuthToolbarPreview() {
    MaterialTheme {
        AuthTopbar(title = "Sign in to your Account")
    }
}

@Preview(showBackground = true)
@Composable
fun AuthToolbarPreviewShortText() {
    MaterialTheme {
        AuthTopbar(title = "Register")
    }
}

@Preview(showBackground = true)
@Composable
fun AuthToolbarPreview2() {
    MaterialTheme {
        AuthTopbar(title = "We need your information")
    }
}

@Preview(showBackground = true)
@Composable
fun AuthToolbarPreviewWithNavigateUp() {
    MaterialTheme {
        AuthTopbar(title = "Sign in to your Account", showNavigateUp = true)
    }
}

@Preview(showBackground = true)
@Composable
fun AuthToolbarPreviewShortWithNavigateUp() {
    MaterialTheme {
        AuthTopbar(title = "Register", showNavigateUp = true)
    }
}