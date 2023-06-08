package com.wesign.wesign.ui.analyze

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.HIGH_RESOLUTION_FLAG_OFF
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.wesign.wesign.ui.theme.WeSignTheme
import com.wesign.wesign.utils.HandLandmarkerHelper
import com.wesign.wesign.utils.ObjectDetectorHelper
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AnalyzerRoute(
    onNavigateUp: () -> Unit,
    onNotGrantedPermission: () -> Unit = {},
    onNotAvailable: () -> Unit = {},
) {

    val viewModel = viewModel<AnalyzerViewModel>()
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
            analyzerState = uiState,
            onUiStateUpdate = viewModel::setState,
            onNavigateUp = onNavigateUp
        )
    }
}


@Composable
fun AnalyzerScreen(
    analyzerState: AnalyzerState = AnalyzerState(),
    onUiStateUpdate: (AnalyzerState) -> Unit = {},
    onNavigateUp: () -> Unit = { },
) {
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var detection by remember { mutableStateOf<Detection?>(null) }

    Scaffold() { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .background(Color.Black)
                .fillMaxSize(),
        ) {
            CameraView(
                modifier = Modifier.matchParentSize(),
                cameraLens = lensFacing,
                onAnalyze = {
                    Log.d("Analyzer","test")
                    detection = it

                },
                onHandLanmark = { result, height, width, runningMode ->
                    val newState = AnalyzerState(result, IntSize(width, height), runningMode)
                    onUiStateUpdate(newState)
                }
            )

            detection?.let {
                Log.d("Analyzer","PASS")
                Box(
                    Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        Modifier.fillMaxSize()
                    ) {
                        Text(
                            "%${it.categories[0].score}",
                            Modifier.fillMaxWidth().weight(1f),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            it.categories[0].label,
                            Modifier.fillMaxWidth().weight(1f),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                }
            }
//            HandOverlayView(
//                modifier = Modifier.matchParentSize(),
//                handLandmarkerResults = analyzerState.handLandmarkerResult,
//                imageHeight = analyzerState.landmarkImageSize.height,
//                imageWidth = analyzerState.landmarkImageSize.width
//            )

            AnalyzerTopBar(
                onNavigateUp = onNavigateUp,
                onSwitchCamera = {
                    lensFacing =
                        if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                }
            )
        }
    }
}

@Composable
private fun CameraView(
    modifier: Modifier = Modifier,
    cameraLens: Int = CameraSelector.LENS_FACING_BACK,
    onAnalyze: (Detection) -> Unit = {},
    onHandLanmark: (HandLandmarkerResult, Int, Int, RunningMode) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember { PreviewView(context) }

    val backgroundExecutor: ExecutorService = remember {
        Executors.newSingleThreadExecutor()
    }

    val executor = ContextCompat.getMainExecutor(context)

    val scope = rememberCoroutineScope()

    val detectorListener = remember {
        object : ObjectDetectorHelper.DetectorListener {
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
                    if (results != null) {
                        for (result in results) {
                            onAnalyze(result)
                            Log.d("Analyzer", result.categories[0].toString())
                        }
                    }
                }
            }
        }
    }


    val objectDetectorHelper: ObjectDetectorHelper = remember {
        ObjectDetectorHelper(context = context, objectDetectorListener = detectorListener)
    }

    val handLandmarkerListener = remember {
        object : HandLandmarkerHelper.LandmarkerListener {
            override fun onError(error: String, errorCode: Int) {
                Log.d("Analyzer - Hand Landmarker", "Error: $error")
            }

            override fun onResults(resultBundle: HandLandmarkerHelper.ResultBundle) {
                onHandLanmark(
                    resultBundle.results.first(),
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM
                )
            }

        }
    }

    var handDetectorHelper: HandLandmarkerHelper? = null

    LaunchedEffect(cameraLens) {
        backgroundExecutor.execute {
            handDetectorHelper = HandLandmarkerHelper(
                context = context,
                runningMode = RunningMode.LIVE_STREAM,
                minHandDetectionConfidence = HandLandmarkerHelper.DEFAULT_HAND_DETECTION_CONFIDENCE,
                minHandTrackingConfidence = HandLandmarkerHelper.DEFAULT_HAND_TRACKING_CONFIDENCE,
                minHandPresenceConfidence = HandLandmarkerHelper.DEFAULT_HAND_PRESENCE_CONFIDENCE,
                maxNumHands = 1,
                handLandmarkerHelperListener = handLandmarkerListener,
                currentDelegate = HandLandmarkerHelper.DELEGATE_CPU
            )
        }

        backgroundExecutor.execute {
            handDetectorHelper!!.clearHandLandmarker()
            handDetectorHelper!!.setupHandLandmarker()
        }

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                        .setHighResolutionEnabledFlag(HIGH_RESOLUTION_FLAG_OFF)
//                        .setResolutionStrategy(ResolutionStrategy(Size()))
                        .build()
                )
                .setTargetRotation(previewView.display.rotation)
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                        .setHighResolutionEnabledFlag(HIGH_RESOLUTION_FLAG_OFF)
                        .build()
                )
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
//                        handDetectorHelper?.let { helper ->
//                            helper.detectLiveStream(
//                                imageProxy = image,
//                                isFrontCamera = cameraLens == CameraSelector.LENS_FACING_FRONT
//                            )
//                        }

                        val bitmapBuffer = Bitmap.createBitmap(
                            image.width,
                            image.height,
                            Bitmap.Config.ARGB_8888
                        )
                        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
                        val imageRotation = image.imageInfo.rotationDegrees
                        // Pass Bitmap and rotation to the object detector helper for processing and detection
                        objectDetectorHelper.detect(bitmapBuffer, imageRotation)
                    }
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraLens)
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview, imageAnalysis
            )
        }, executor)
    }


    Box(Modifier.fillMaxSize()) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                previewView
            },
        )
    }
}

@Composable
private fun AnalyzerTopBar(
    onNavigateUp: () -> Unit,
    onSwitchCamera: () -> Unit
) {
    Row(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(45.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onNavigateUp,
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    Color.Black.copy(
                        alpha = 0.65f
                    ),
                    shape = CircleShape
                ),
        ) {
            Image(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        IconButton(
            onClick = onSwitchCamera,
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(
                    Color.Black.copy(
                        alpha = 0.65f
                    ),
                    shape = CircleShape
                ),
        ) {
            Image(
                Icons.Filled.Cameraswitch,
                contentDescription = "Back",
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(Color.White)
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

