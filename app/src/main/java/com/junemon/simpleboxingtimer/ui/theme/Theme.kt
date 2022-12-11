package com.junemon.simpleboxingtimer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Rally is always dark themed.
val ColorPalette = darkColors(
    primary = Green500,
    surface = DarkBlue900,
    onSurface = Color.White,
    background = DarkBlue900,
    onBackground = Color.White
)

@Composable
fun TimerTheme(content: @Composable () -> Unit) {
    //always start in dark mode
    MaterialTheme(colors = ColorPalette, typography = Typography, content = content)
}
