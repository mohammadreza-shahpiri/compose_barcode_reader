# JetpackCompose BarcodeScanner Android Library

How to use it:

```kotlin
 CameraPreview(
    barcodeResult = {
       // Do something with result…
    },
    analyser = uiState.analyzer
) {
    // Handle back button press
}
```