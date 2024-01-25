package com.github.compose.barcodereader.camera

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.compose.barcodereader.utils.drawRect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Immutable
enum class Analyzer {
    MLKIT, ZXING
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreview(
    barcodeResult: (barcode: List<String>) -> Unit,
    analyser: Analyzer = Analyzer.ZXING,
    backHandle: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val isGranted = cameraPermissionState.status.isGranted
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawRect(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (isGranted) {
            CameraXPreview(
                barcodeResult = barcodeResult,
                analyser = analyser,
                backHandle = backHandle
            )
        } else {
            Button(onClick = {
                cameraPermissionState.launchPermissionRequest()
            }){
                Text(text = "get camera permission")
            }
        }
    }
}
