package com.wesign.wesign.ui.analyze

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.wesign.wesign.component.MLCameraView
import com.wesign.wesign.ui.analyze.common.AnalyzerTopBar
import com.wesign.wesign.ui.theme.WeSignTheme
import com.wesign.wesign.utils.ObjectDetectorHelper
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.detector.Detection

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AnalyzerRoute(
    viewModel: AnalyzerViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onNotGrantedPermission: () -> Unit = {},
    onNotAvailable: () -> Unit = {},
    onSettingPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val PERMISSION = listOf(
        Manifest.permission.CAMERA,
    )

    val permissionState =
        rememberMultiplePermissionsState(permissions = PERMISSION)

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Log.d("CameraML", "Not Granted")
            onNotGrantedPermission()
        },
        permissionsNotAvailableContent = {
            Log.d("CameraML", "Not Available")
            onNotAvailable()
        },
    ) {
        AnalyzerScreen(
            uiState = uiState,
            onNavigateUp = onNavigateUp,
            onAddDetectionHistory = viewModel::addHistory,
            onInitHelper = viewModel::setObjectDetectorHelper,
            onSettingPressed = onSettingPressed
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzerScreen(
    uiState: AnalyzerState = AnalyzerState(),
    onAddDetectionHistory: (Detection) -> Unit = {},
    onNavigateUp: () -> Unit = { },
    onInitHelper: (ObjectDetectorHelper) -> Unit = {},
    onSettingPressed: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var firstTime by remember { mutableStateOf(true) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val listener = object : ObjectDetectorHelper.DetectorListener {
        override fun onError(error: String) {
            Log.d("Analyzer", "Error: $error")
        }

        override fun onResults(
            results: MutableList<Detection>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        ) {
            scope.launch {
                Log.d("Analyzer", "inferenceTime: $inferenceTime")
                results?.let {
                    if (results.size <= 0) {
                        return@let
                    }

                    for (detect in results) {
                        if(detect.categories[0].score <= 0.85) return@let
                        onAddDetectionHistory(detect)
                        if (firstTime) {
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                            firstTime = false
                        }
                    }
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 80.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    if (uiState.detectionHistory.isNotEmpty()) {
                        val reversedList = uiState.detectionHistory.reversed()
                        val latestDetection = reversedList.first()
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 3.dp),
                            Arrangement.SpaceBetween
                        ) {
                            Text(
                                latestDetection.categories[0].label.capitalize(Locale.current),
                                Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .padding(vertical = 3.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                uiState.selectedSignModel.name,
                                Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                                    .padding(vertical = 3.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LazyColumn(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(reversedList.minus(latestDetection)) {
                                Text(
                                    it.categories[0].label.capitalize(Locale.current),
                                    Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(vertical = 2.dp),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        },
    ) { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .background(Color.Black)
                .fillMaxSize(),
        ) {
            MLCameraView(
                modifier = Modifier.matchParentSize(),
                cameraLens = lensFacing,
                objectDetectorHelper = uiState.objectDetectorHelper ?: run {
                    Log.d("Analyzer", "Init ObjectDetectorHelper")
                    val objectHelper = ObjectDetectorHelper(
                        context = context,
                        objectDetectorListener = listener,
                        signModel = uiState.selectedSignModel
                    )
                    onInitHelper(objectHelper)
                    return@run objectHelper
                }
            )

            AnalyzerTopBar(
                onNavigateUp = onNavigateUp,
                onSwitchCamera = {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                },
                onSettingPressed = onSettingPressed
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AnalyzerScreenPreview() {
    WeSignTheme {
        AnalyzerScreen()
    }
}

