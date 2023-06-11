package com.wesign.wesign.ui.analyze

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.HIGH_RESOLUTION_FLAG_OFF
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.wesign.wesign.ui.theme.WeSignTheme
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
            uiState = uiState,
            onNavigateUp = onNavigateUp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzerScreen(
    uiState: AnalyzerState = AnalyzerState(),
    onNavigateUp: () -> Unit = { },
) {
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var detection by remember { mutableStateOf<Detection?>(null) }
    var lastestSize by remember { mutableStateOf<IntSize>(IntSize(0, 0)) }
    var lastestInferenceTime by remember { mutableStateOf<Long>(0) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 80.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Development Phase:")
                    detection?.let {
                        Log.d("Analyzer", it.categories[0].label)
                        Text(it.categories[0].label)
                        Text("${(it.categories[0].score * 100).toInt()}")
                        Text("InferenceTime: ${lastestInferenceTime}")
                        Text("Size: $lastestSize")
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
            CameraView(
                modifier = Modifier.matchParentSize(),
                cameraLens = lensFacing,
                onAnalyze = { result, inferenceTime, size ->
                    detection = result
                    lastestInferenceTime = inferenceTime
                    lastestSize = size
                },
            )

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
    onAnalyze: (Detection?, Long, IntSize) -> Unit = { _, _, _ -> },
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
                    results?.let {
                        if (results.size <= 0) {
                            Log.d("Analyzer", "Size Detection 0")
                            onAnalyze(null, inferenceTime, IntSize(imageWidth, imageHeight))
                            return@let
                        }

                        for (detect in results) {
                            Log.d("Analyzer", "Subject")
                            onAnalyze(detect, inferenceTime, IntSize(imageWidth, imageHeight))
                        }
                    } ?: run {
                        Log.d("Analyzer", "No Subject")
                        onAnalyze(null, inferenceTime, IntSize(imageWidth, imageHeight))
                    }
                }
            }
        }
    }


    val objectDetectorHelper: ObjectDetectorHelper = remember {
        ObjectDetectorHelper(
            context = context,
            objectDetectorListener = detectorListener,
        )
    }

    LaunchedEffect(cameraLens) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                        .setHighResolutionEnabledFlag(HIGH_RESOLUTION_FLAG_OFF)
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
                        .setResolutionStrategy(ResolutionStrategy(Size(640,480),FALLBACK_RULE_CLOSEST_HIGHER))
                        .setHighResolutionEnabledFlag(HIGH_RESOLUTION_FLAG_OFF)
                        .build()
                )
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        val bitmapBuffer = Bitmap.createBitmap(
                            image.width,
                            image.height,
                            Bitmap.Config.ARGB_8888
                        )
                        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
                        val imageRotation = image.imageInfo.rotationDegrees
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

