package com.wesign.wesign.component

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.wesign.wesign.utils.ObjectDetectorHelper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun MLCameraView(
    modifier: Modifier = Modifier,
    cameraLens: Int = CameraSelector.LENS_FACING_BACK,
    objectDetectorHelper: ObjectDetectorHelper,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember { PreviewView(context) }

    val backgroundExecutor: ExecutorService = remember {
        Executors.newSingleThreadExecutor()
    }

    val executor = ContextCompat.getMainExecutor(context)

    LaunchedEffect(cameraLens) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setResolutionSelector(
                    ResolutionSelector.Builder()
                        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                        .setHighResolutionEnabledFlag(ResolutionSelector.HIGH_RESOLUTION_FLAG_OFF)
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
                        .setResolutionStrategy(
                            ResolutionStrategy(
                                Size(640, 360),
                                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER
                            ),
                        )
                        .setHighResolutionEnabledFlag(ResolutionSelector.HIGH_RESOLUTION_FLAG_OFF)
                        .build()
                )
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        val bitmapBuffer = Bitmap.createBitmap(
                            image.width,
                            image.height,
                            Bitmap.Config.ARGB_8888
                        )
                        try {
                            image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
                        } catch (ex: RuntimeException) {

                        }
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