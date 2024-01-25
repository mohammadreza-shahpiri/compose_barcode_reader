package com.github.compose.barcodereader.analyze

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.github.compose.barcodereader.utils.extractBarcodes
import com.github.compose.barcodereader.utils.update
import de.markusfisch.android.zxingcpp.ZxingCpp

class ZxingBarCodeAnalyser(
    private val onBarcodeDetected: (barcode: List<String>) -> Unit
) : ImageAnalysis.Analyzer {
    private var isDetecting = false
    private val decodeHints = ZxingCpp.ReaderOptions().apply {
        tryHarder = true
        tryRotate = true
        tryInvert = true
        tryDownscale = true
        maxNumberOfSymbols = 1
        formats = ZxingCpp.BarcodeFormat.entries.toSet()
    }

    override fun analyze(image: ImageProxy) {
        if (isDetecting) {
            image.close()
            return
        }
        isDetecting = true
        val cropRect = Rect()
        cropRect.update(image.width, image.height)
        image.setCropRect(cropRect)
        val result = image.extractBarcodes(decodeHints)
        image.close()
        isDetecting=false
        if (result.isNullOrEmpty()) return
        onBarcodeDetected(result.map {
            it.text
        })
    }
}


















