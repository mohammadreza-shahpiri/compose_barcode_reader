package com.github.compose.barcodereader.camera.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.compose.barcodereader.R

@Composable
fun Overlay() {
    val wDp = 300.dp
    val hDp = 200.dp
    val widthInPx: Float
    val heightInPx: Float
    with(LocalDensity.current) {
        widthInPx = wDp.toPx()
        heightInPx = hDp.toPx()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                drawRect(Color(0x77000000))
                drawRoundRect(
                    topLeft = Offset(
                        x = (canvasWidth - widthInPx) / 2,
                        y = (canvasHeight - heightInPx) / 2
                    ),
                    size = Size(widthInPx, heightInPx),
                    cornerRadius = CornerRadius(30f, 30f),
                    color = Color.Transparent,
                    blendMode = BlendMode.Clear
                )
                restoreToCount(checkPoint)
            }
        }
        LottieLoader(
            modifier = Modifier
                .width(wDp)
                .height(hDp),
            animation = R.raw.scan,
            speed = 0.5f
        )
    }
}