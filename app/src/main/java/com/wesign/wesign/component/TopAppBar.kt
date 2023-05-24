package com.wesign.wesign.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesign.wesign.ui.theme.LocalExtendedColorScheme
import com.wesign.wesign.ui.theme.WeSignTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onNavigateBack: (() -> Unit)? = null,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalExtendedColorScheme.current.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),

        navigationIcon = {
            onNavigateBack?.let {
                IconButton(onClick = it) {
                    Image(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(10.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        },
    )

}

@Preview()
@Composable
fun MyTopAppBarPreview() {
    WeSignTheme {
        MyTopAppBar(title = "Course Detail")
    }
}

@Preview
@Composable
fun MyTopAppBarWithNavBackPreview() {
    WeSignTheme {
        MyTopAppBar(title = "Course Detail", onNavigateBack = {})
    }
}