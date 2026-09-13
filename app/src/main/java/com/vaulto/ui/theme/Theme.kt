package com.campus.vaulto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Accent, onPrimary = CardWhite,
    primaryContainer = AccentSoft, onPrimaryContainer = Ink,
    secondary = Slate, tertiary = Rose, background = Paper, onBackground = Ink,
    surface = CardWhite, onSurface = Ink,
    surfaceVariant = Mist, onSurfaceVariant = Slate,
    outline = Color(0xFFD0D5DD), error = Color(0xFFB42318)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB7B4FF), onPrimary = Color(0xFF24205F),
    primaryContainer = Color(0xFF373276), onPrimaryContainer = Color(0xFFE4E2FF),
    tertiary = Color(0xFFFFB0D8),
    background = Night, onBackground = Color(0xFFF2F4F7),
    surface = Color(0xFF1D2939), onSurface = Color(0xFFF2F4F7),
    surfaceVariant = Color(0xFF344054), onSurfaceVariant = Color(0xFFD0D5DD),
    error = Color(0xFFFDA29B)
)

@Composable
fun VaultoTheme(themeMode: ThemeMode = ThemeMode.DARK, content: @Composable () -> Unit) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme, typography = Typography, content = content)
}
