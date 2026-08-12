package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.domain.model.ThemeMode

data class GlassColorScheme(
    val canvasBackground: Color,
    val glassSurface: Color,
    val glassSurfaceHover: Color,
    val glassBorder: Color,
    val glassBorderHighlight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val isDark: Boolean
)

val LocalGlassColorScheme = staticCompositionLocalOf {
    GlassColorScheme(
        canvasBackground = DarkCanvas,
        glassSurface = DarkSurfaceGlass,
        glassSurfaceHover = DarkSurfaceGlassHover,
        glassBorder = DarkBorderGlass,
        glassBorderHighlight = DarkBorderGlassHighlight,
        textPrimary = TextPrimaryDark,
        textSecondary = TextSecondaryDark,
        isDark = true
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryViolet,
    secondary = SecondaryCyan,
    tertiary = AccentPink,
    background = DarkCanvas,
    surface = DarkSurfaceGlass,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryVioletLight,
    secondary = SecondaryCyanLight,
    tertiary = AccentPink,
    background = LightCanvas,
    surface = LightSurfaceGlass,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

@Composable
fun GlassicPlayerTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val glassColorScheme = if (darkTheme) {
        GlassColorScheme(
            canvasBackground = DarkCanvas,
            glassSurface = DarkSurfaceGlass,
            glassSurfaceHover = DarkSurfaceGlassHover,
            glassBorder = DarkBorderGlass,
            glassBorderHighlight = DarkBorderGlassHighlight,
            textPrimary = TextPrimaryDark,
            textSecondary = TextSecondaryDark,
            isDark = true
        )
    } else {
        GlassColorScheme(
            canvasBackground = LightCanvas,
            glassSurface = LightSurfaceGlass,
            glassSurfaceHover = LightSurfaceGlassHover,
            glassBorder = LightBorderGlass,
            glassBorderHighlight = LightBorderGlassHighlight,
            textPrimary = TextPrimaryLight,
            textSecondary = TextSecondaryLight,
            isDark = false
        )
    }

    val materialColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalGlassColorScheme provides glassColorScheme) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}
