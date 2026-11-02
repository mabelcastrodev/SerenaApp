package com.mabel.serenaapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(

    // Principal
    primary = AppPrimary,
    onPrimary = AppSurface,

    // Secundario (amarillo)
    secondary = AppAccent,
    onSecondary = AppForeground,

    // Acento
    tertiary = AppAccent,
    onTertiary = AppForeground,

    // Fondo
    background = AppBackground,
    onBackground = AppForeground,

    // Superficies
    surface = AppSurface,
    onSurface = AppForeground,

    // Variante de superficie
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = AppMuted,

    // Bordes
    outline = AppMuted
)

private val DarkColorScheme = darkColorScheme(

    // Principal
    primary = AppDarkAccentBlue,
    onPrimary = AppSurface,

    // Secundario (el amarillo no cambia)
    secondary = AppAccent,
    onSecondary = AppForeground,

    // Acento
    tertiary = AppAccent,
    onTertiary = AppForeground,

    // Fondo
    background = AppDark,
    onBackground = AppSurface,

    // Superficies (barras y tarjetas)
    surface = AppDarkSurface,
    onSurface = AppSurface,

    // Variante de superficie
    surfaceVariant = AppDarkSurfaceVariant,
    onSurfaceVariant = AppDarkMuted,

    // Bordes
    outline = AppDarkMuted
)

@Composable
fun SerenaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object AppTheme {
    val spacing: AppSpacing
        @Composable @ReadOnlyComposable get() = LocalAppSpacing.current

    val sizes: AppComponentSizes
        @Composable @ReadOnlyComposable get() = LocalAppComponentSizes.current
}
