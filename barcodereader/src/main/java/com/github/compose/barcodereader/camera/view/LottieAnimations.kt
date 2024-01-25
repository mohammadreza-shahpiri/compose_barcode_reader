package com.github.compose.barcodereader.camera.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    animation: Int,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animation)
    )
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        clipToCompositionBounds = true,
        enableMergePaths = true,
        speed = speed,
        iterations = LottieConstants.IterateForever
    )
}