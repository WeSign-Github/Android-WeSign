package com.wesign.wesign.ui.texttosign

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.data.entity.TextToSignResponse
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun TextToSignRoute(
    viewModel: TextToSignViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onGeneratePressed: (List<TextToSignResponse.SignWord>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TextToSignScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSelectedWordClick = viewModel::removeWord,
        onWordClick = viewModel::appendWord,
        onTryAgainPressed = viewModel::tryAgain,
        onGeneratePressed = onGeneratePressed
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TextToSignScreen(
    uiState: TextToSignState = TextToSignState(),
    onNavigateBack: () -> Unit = {},
    onSelectedWordClick: (TextToSignResponse.SignWord) -> Unit = {},
    onWordClick: (TextToSignResponse.SignWord) -> Unit = {},
    onTryAgainPressed: () -> Unit = {},
    onGeneratePressed: (List<TextToSignResponse.SignWord>) -> Unit = {}
) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Text to Sign",
                onNavigateBack = onNavigateBack
            )
        }
    ) { contentPadding ->

        if (uiState.isTryAgain) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column() {
                    Text(
                        "Request Timeout",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(Modifier.height(5.dp))

                    ElevatedButton(
                        onClick = onTryAgainPressed, colors = ButtonDefaults.elevatedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Try Again!", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(20.dp)
        ) {

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                uiState.selectedWord.forEach { signWord ->
                    BoxButton(
                        Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(3.dp),
                        selected = true,
                        onClick = {
                            onSelectedWordClick(signWord)
                        },
                        text = signWord.word
                    )
                }
            }

            Spacer(Modifier.height(25.dp))

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {

                if (uiState.isLoading) {
                    (0..15).forEach { _ ->
                        BoxButton(
                            Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(3.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                ),
                            text = "Loading"
                        )
                    }
                } else {
                    uiState.listWord?.let {
                        it.data.filter { item ->
                            !uiState.selectedWord.contains(item)
                        }.forEach { signWord ->
                            BoxButton(
                                Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .padding(4.dp),
                                onClick = {
                                    onWordClick(signWord)
                                },
                                text = signWord.word
                            )
                        }
                    }
                }
            }

            if (!uiState.isLoading) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Button(
                        onClick = { onGeneratePressed(uiState.selectedWord) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Generate")
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Box(
        modifier
            .clip(RoundedCornerShape(percent = 50))
            .clickable { onClick() }
            .background(
                color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(percent = 50)
            )
            .border(
                .5.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun TextToSignScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        TextToSignScreen(
            uiState = TextToSignState(
                selectedWord = listOf(
                    TextToSignResponse.SignWord(0, "Saya", "", "", ""),
                    TextToSignResponse.SignWord(0, "Suka", "", "", ""),
                    TextToSignResponse.SignWord(0, "Makan", "", "", ""),
                    TextToSignResponse.SignWord(0, "Ayam", "", "", ""),
                ),
                listWord = TextToSignResponse(
                    listOf(
                        TextToSignResponse.SignWord(0, "Saya", "", "", ""),
                        TextToSignResponse.SignWord(0, "Suka", "", "", ""),
                        TextToSignResponse.SignWord(0, "Makan", "", "", ""),
                        TextToSignResponse.SignWord(0, "Ayam", "", "", ""),
                        TextToSignResponse.SignWord(0, "TEST AAA", "", "", ""),
                        TextToSignResponse.SignWord(0, "Ayam GORENNG", "", "", ""),
                        TextToSignResponse.SignWord(0, "Teks panjang", "", "", ""),
                        TextToSignResponse.SignWord(0, "Awesome", "", "", ""),
                    ), "", false
                )
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun TextToSignScreenPreviewLoading() {
    WeSignTheme(dynamicColor = false) {
        TextToSignScreen(uiState = TextToSignState(isLoading = true))
    }
}

@Preview(showSystemUi = true)
@Composable
fun TextToSignScreenPreviewTryAgain() {
    WeSignTheme(dynamicColor = false) {
        TextToSignScreen(uiState = TextToSignState(isTryAgain = true))
    }
}