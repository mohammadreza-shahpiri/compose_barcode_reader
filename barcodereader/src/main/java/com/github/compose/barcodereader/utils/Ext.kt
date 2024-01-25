package com.github.compose.barcodereader.utils

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import de.markusfisch.android.zxingcpp.ZxingCpp

fun ImageProxy.extractBarcodes(
    options: ZxingCpp.ReaderOptions
) = runCatching {
    ZxingCpp.readByteArray(
        luminancePlaneData(),
        planes[0].rowStride,
        cropRect,
        imageInfo.rotationDegrees,
        options
    )
}.getOrNull()

private fun ImageProxy.luminancePlaneData(): ByteArray {
    val plane = planes[0]
    val buf = plane.buffer
    val data = ByteArray(buf.remaining())
    buf.get(data)
    buf.rewind()
    val rowStride = plane.rowStride
    val pixelStride = plane.pixelStride
    val cleanData = ByteArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            cleanData[y * width + x] = data[y * rowStride + x * pixelStride]
        }
    }
    return cleanData
}

@SuppressLint("RestrictedApi", "VisibleForTests")
fun ProcessCameraProvider?.tryShutdown() = runCatching {
    this?.run {
        unbindAll()
        shutdown()
    }
}

fun Rect.update(width: Int, height: Int){
    val cropSize = height / 3 * 2
    val left = ((width - cropSize) / 2)*1.2
    val top = ((height - cropSize) / 2)*1.2
    set(left.toInt(), top.toInt(), left.toInt() + cropSize, top.toInt() + cropSize)
}

fun getFlashIcon(enabled: Boolean) =
    if (enabled) Icons.Default.FlashOn else Icons.Default.FlashOff



@Stable
fun Modifier.drawRect(color: Color) = drawBehind {
    drawRect(color = color)
}

@Composable
fun DisposableEffectTrue(onDispose: () -> Unit) {
    DisposableEffect(true) {
        onDispose {
            onDispose()
        }
    }
}