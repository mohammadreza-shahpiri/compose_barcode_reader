package com.github.compose.barcodereader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.compose.barcodereader.camera.Analyzer
import com.github.compose.barcodereader.camera.CameraPreview
import com.github.compose.barcodereader.ui.theme.ComposeBarcodeReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBarcodeReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

data class UiState(
    val showCamera: Boolean = false,
    val analyzer: Analyzer = Analyzer.ZXING,
    val barcodes: ImmutableList<String> = persistentListOf()
)

fun Analyzer.toText() = if (this == Analyzer.MLKIT) "MLKIT" else "ZXING"
fun String.toAnalyzer() = if (this == "MLKIT") Analyzer.MLKIT else Analyzer.ZXING

@Composable
fun MainContent() {
    var uiState by remember { mutableStateOf(UiState()) }
    val radioOptions = listOf("ZXING", "MLKIT")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.showCamera) {
            CameraPreview(
                barcodeResult = {
                    uiState = uiState.copy(
                        showCamera = false,
                        barcodes = it.toImmutableList()
                    )
                },
                analyser = uiState.analyzer
            ) {
                uiState = uiState.copy(showCamera = false)
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Barcodes=: ",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = uiState.barcodes.joinToString(separator = "\n"),
                    color = Color.Red
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = Color.Black,
                        text = "Analyzer: "
                    )
                    radioOptions.forEach { fruitName ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (fruitName == uiState.analyzer.toText()),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Green
                                ),
                                onClick = {
                                    uiState = uiState.copy(
                                        analyzer = fruitName.toAnalyzer()
                                    )
                                }
                            )
                            Text(
                                text = fruitName,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                Button(onClick = {
                    uiState = uiState.copy(showCamera = true)
                }) {
                    Text(text = "Open Camera")
                }
            }
        }
    }
}