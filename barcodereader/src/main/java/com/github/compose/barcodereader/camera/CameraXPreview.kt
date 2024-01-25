package com.github.compose.barcodereader.camera

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.compose.BackHandler
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.compose.barcodereader.analyze.MlkitBarCodeAnalyser
import com.github.compose.barcodereader.analyze.ZxingBarCodeAnalyser
import com.github.compose.barcodereader.camera.view.Overlay
import com.github.compose.barcodereader.utils.DisposableEffectTrue
import com.github.compose.barcodereader.utils.getFlashIcon
import com.github.compose.barcodereader.utils.tryShutdown
import java.util.concurrent.Executors

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraXPreview(
    barcodeResult: (barcode: List<String>) -> Unit,
    analyser: Analyzer,
    backHandle: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var cameraProvider: ProcessCameraProvider? = null
    DisposableEffectTrue {
        cameraProvider.tryShutdown()
    }
    BackHandler {
        backHandle()
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.Black.hashCode())
                        implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
                update = { previewView ->
                    val cameraExecutor = Executors.newSingleThreadExecutor()
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val preview =
                            Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                        cameraProvider = cameraProviderFuture.get()
                        val barcodeAnalyserZxing = if (analyser == Analyzer.ZXING){
                            ZxingBarCodeAnalyser {
                                barcodeResult(it)
                            }
                        }else{
                            MlkitBarCodeAnalyser{
                                barcodeResult(it)
                            }
                        }
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetRotation(preview.targetRotation)
                            .setImageQueueDepth(1)
                            .build()
                            .also { imageAnalysis ->
                                imageAnalysis.setAnalyzer(cameraExecutor, barcodeAnalyserZxing)
                            }
                        cameraProvider?.unbindAll()
                        cameraProvider?.bindToLifecycle(
                            lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA,
                            preview, imageAnalysis
                        )
                    }, ContextCompat.getMainExecutor(context))
                }
            )
            Overlay()
            CameraFlashIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                cameraController = cameraController
            )
        }
    }
}

@Stable
@Composable
fun CameraFlashIcon(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController
) {
    val torchState = cameraController.torchState.observeAsState()
    val enabled = (TorchState.ON == torchState.value)
    val torch = getFlashIcon(enabled)
    Icon(
        modifier = modifier
            .clickable {
                cameraController.enableTorch(!enabled)
            },
        imageVector = torch,
        contentDescription = null,
        tint = Color.White
    )
}