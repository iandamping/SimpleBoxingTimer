package com.junemon.simpleboxingtimer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Rally is always dark themed.
private val LightSunnyColorScheme = lightColorScheme(
    primary = PrimarySunnySky,
    primaryContainer = PrimaryContainerSunnySky,
    surface = SurfaceSunnySky,
    background = BackgroundSunnySky,
    secondary = SecondarySunnySky
)

private val DarkSunnyColorScheme = lightColorScheme(
    primary = DarkPrimarySunnySky,
    primaryContainer = DarkPrimaryContainerSunnySky,
    surface = DarkSurfaceSunnySky,
    background = DarkBackgroundSunnySky,
    secondary = DarkSecondarySunnySky
)

private val LightCloudyColorScheme = lightColorScheme(
    primary = PrimaryCloudy,
    primaryContainer = PrimaryContainerCloudy,
    surface = SurfaceCloudy,
    background = BackgroundCloudy,
    tertiary = TertiaryCloudy
)

private val DarkCloudyColorScheme = lightColorScheme(
    primary = DarkPrimaryCloudy,
    primaryContainer = DarkPrimaryContainerCloudy,
    surface = DarkSurfaceCloudy,
    background = DarkBackgroundCloudy,
    tertiary = DarkTertiaryCloudy
)

private val LightStormyColorScheme = lightColorScheme(
    primary = PrimaryStormy,
    primaryContainer = PrimaryContainerStormy,
    surface = SurfaceStormy,
    background = BackgroundStormy,
    secondary = SecondaryStormy
)

private val DarkStormyColorScheme = lightColorScheme(
    primary = DarkPrimaryStormy,
    primaryContainer = DarkPrimaryContainerStormy,
    surface = DarkSurfaceStormy,
    background = DarkBackgroundStormy,
    secondary = DarkSecondaryStormy
)

@Composable
fun TimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    //always start in dark mode
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkSunnyColorScheme
        else -> LightSunnyColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SimpleTimerTypography,
        shapes = SimpleTimerShapes,
        content = content
    )
}
