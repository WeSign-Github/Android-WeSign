package com.wesign.wesign.ui.texttosign.generate

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.data.entity.TextToSignResponse
import com.wesign.wesign.ui.texttosign.TextToSignState
import com.wesign.wesign.ui.texttosign.TextToSignViewModel
import com.wesign.wesign.ui.theme.WeSignTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GenerateSignRoute(
    viewModel: TextToSignViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d("Generate", uiState.selectedWord.toString())

    GenerateSignScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )

}

@Composable
fun GenerateSignScreen(
    uiState: TextToSignState = TextToSignState(),
    onNavigateBack: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }
    var isStillLoading by remember { mutableStateOf(true) }

    LaunchedEffect(currentIndex, isStillLoading) {
        scope.launch {
            if (isStillLoading) return@launch
            delay(2000)
            currentIndex = (currentIndex + 1).mod(uiState.selectedWord.size)
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Text to Sign",
                onNavigateBack = onNavigateBack
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.selectedWord[currentIndex].let {
                    Text(it.word)
                    AsyncImage(
                        model = it.image,
                        contentDescription = it.word,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .placeholder(
                                isStillLoading,
                                highlight = PlaceholderHighlight.shimmer()
                            ),
                        onLoading = {
                            isStillLoading = true
                        },
                        onSuccess = {
                            isStillLoading = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                ) {
                    uiState.selectedWord.forEachIndexed { index, _ ->
                        Box(
                            Modifier
                                .padding(3.dp)
                                .fillMaxHeight()
                                .weight(1f)
                                .background(if (index == currentIndex) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryContainer)
                        )
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }

        }
    }
}


@Preview
@Composable
private fun GenerateSignScreen() {
    WeSignTheme(dynamicColor = false) {
        GenerateSignScreen(
            uiState = TextToSignState(
                selectedWord = listOf(
                    TextToSignResponse.SignWord(0, "Saya", "", "", ""),
                    TextToSignResponse.SignWord(0, "Suka", "", "", ""),
                    TextToSignResponse.SignWord(0, "Makan", "", "", ""),
                    TextToSignResponse.SignWord(0, "Ayam", "", "", ""),
                )
            )
        )
    }

}