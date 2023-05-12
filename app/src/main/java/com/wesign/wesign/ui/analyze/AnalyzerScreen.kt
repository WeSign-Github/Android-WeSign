package com.wesign.wesign.ui.analyze

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.wesign.wesign.ui.theme.WeSignTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AnalyzerRoute(
    onNavigateUp: () -> Unit,
    onNotGrantedPermission: () -> Unit = {},
    onNotAvailable: () -> Unit = {},
) {

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
        },
        permissionsNotAvailableContent = {
            Log.d("CameraML", "Not Available")
        },
    ) {
        AnalyzerScreen(onNavigateUp = onNavigateUp)
    }
}


@Composable
fun AnalyzerScreen(
    onNavigateUp: () -> Unit = { },
) {
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }

    Scaffold() { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .background(Color.Black)
                .fillMaxSize(),
        ) {
            CameraView(modifier = Modifier.fillMaxSize(), cameraLens = lensFacing)
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
    cameraLens: Int = CameraSelector.LENS_FACING_BACK
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember { PreviewView(context) }
    val executor = ContextCompat.getMainExecutor(context)

    LaunchedEffect(cameraLens) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(cameraLens)
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        }, executor)
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            previewView
        },
    )
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

