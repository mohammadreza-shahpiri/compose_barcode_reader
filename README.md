# JetpackCompose BarcodeScanner

How to use it:

```kotlin
 CameraPreview(
    barcodeResult = {
       // Do something with result…
    },
    analyser = Analyzer.ZXING // Analyzer.MLKIT
) {
    // Handle back button press
}
```