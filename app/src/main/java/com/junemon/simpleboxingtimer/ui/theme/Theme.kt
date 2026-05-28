package com.junemon.simpleboxingtimer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightSunnyColorScheme = lightColorScheme(
    primary = PrimarySunnySky,
    onPrimary = OnPrimarySunnySky,
    primaryContainer = PrimaryContainerSunnySky,
    onPrimaryContainer = OnPrimaryContainerSunnySky,
    secondary = SecondarySunnySky,
    onSecondary = OnSecondarySunnySky,
    secondaryContainer = SecondaryContainerSunnySky,
    onSecondaryContainer = OnSecondaryContainerSunnySky,
    surface = SurfaceSunnySky,
    onSurface = OnSurfaceSunnySky,
    surfaceVariant = SurfaceVariantSunnySky,
    onSurfaceVariant = OnSurfaceVariantSunnySky,
    background = BackgroundSunnySky,
    onBackground = OnBackgroundSunnySky,
    outline = OutlineSunnySky
)

private val DarkSunnyColorScheme = darkColorScheme(
    primary = DarkPrimarySunnySky,
    onPrimary = DarkOnPrimarySunnySky,
    primaryContainer = DarkPrimaryContainerSunnySky,
    onPrimaryContainer = DarkOnPrimaryContainerSunnySky,
    secondary = DarkSecondarySunnySky,
    onSecondary = DarkOnSecondarySunnySky,
    secondaryContainer = DarkSecondaryContainerSunnySky,
    onSecondaryContainer = DarkOnSecondaryContainerSunnySky,
    surface = DarkSurfaceSunnySky,
    onSurface = DarkOnSurfaceSunnySky,
    surfaceVariant = DarkSurfaceVariantSunnySky,
    onSurfaceVariant = DarkOnSurfaceVariantSunnySky,
    background = DarkBackgroundSunnySky,
    onBackground = DarkOnBackgroundSunnySky,
    outline = DarkOutlineSunnySky
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
