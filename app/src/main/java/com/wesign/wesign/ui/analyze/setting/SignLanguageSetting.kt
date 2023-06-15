package com.wesign.wesign.ui.analyze.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesign.wesign.component.MyTopAppBar
import com.wesign.wesign.core.SignLanguageModels
import com.wesign.wesign.ui.analyze.AnalyzerState
import com.wesign.wesign.ui.analyze.AnalyzerViewModel
import com.wesign.wesign.ui.theme.WeSignTheme


@Composable
fun SignLanguageSettingRoute(
    viewModel: AnalyzerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignLanguageSettingScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onItemClicked = { model ->
            viewModel.setObjectDetectorHelper(null)
            viewModel.setSignModel(model)
        }
    )
}

@Composable
fun SignLanguageSettingScreen(
    uiState: AnalyzerState = AnalyzerState(),
    onNavigateBack: () -> Unit = {},
    onItemClicked: (SignLanguageModels) -> Unit = {}
) {
    Scaffold(
        topBar = {
            MyTopAppBar(title = "Sign Language Setting", onNavigateBack = onNavigateBack)
        }
    ) { contentPadding ->
        Column(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(text = "Change Sign Language", style = MaterialTheme.typography.titleMedium)
            SignLanguageItem(
                Modifier.fillMaxWidth(),
                title = "SIBI",
                selected = uiState.selectedSignModel == SignLanguageModels.SIBI,
                onItemClicked = {
                    onItemClicked(SignLanguageModels.SIBI)
                },
            )
            SignLanguageItem(
                Modifier.fillMaxWidth(),
                title = "BISINDO",
                selected = uiState.selectedSignModel == SignLanguageModels.BISINDO_NEW,
                onItemClicked = {
                    onItemClicked(SignLanguageModels.BISINDO_NEW)
                },
            )
        }
    }
}

@Composable
private fun SignLanguageItem(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onItemClicked: () -> Unit,
) {
    Row(modifier, Arrangement.Start, Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onItemClicked)
        Spacer(Modifier.height(25.dp))
        Text(title)
    }

}

@Preview
@Composable
private fun SignLanguageSettingScreenPreview() {
    WeSignTheme(dynamicColor = false) {
        SignLanguageSettingScreen()
    }
}