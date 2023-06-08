package com.wesign.wesign.ui.feature.learning.course.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.ui.theme.WeSignTheme
import kotlinx.coroutines.launch


@Composable
fun LessonRoute() {
    LessonScreen()
}

@Composable
fun LessonScreen(
    onNavigateBack: () -> Unit = {},
    onListLessonPressed: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        LessonDrawerContent()
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = "Text to Sign",
                            onNavigateBack = onNavigateBack,
                            actions = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.List,
                                        contentDescription = "Lesson List"
                                    )
                                }
                            }
                        )
                    },
                ) { contentPadding ->

                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(20.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Word",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            AsyncImage(
                                model = "",
                                contentDescription = "",
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(Color.DarkGray),
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
                                onClick = {},
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
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(text = "Next")
                            }
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LessonScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        LessonScreen()
    }
}