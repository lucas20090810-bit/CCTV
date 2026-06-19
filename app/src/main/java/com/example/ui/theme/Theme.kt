package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = NeonRed,
    secondary = DarkCardBg,
    tertiary = GoldTop10,
    background = DarkSlateBg,
    surface = DarkCardBg,
    onPrimary = Color.White,
    onSecondary = AccentWhite,
    onTertiary = DarkSlateBg,
    onBackground = AccentWhite,
    onSurface = AccentWhite,
    outline = SlateGrey
  )

private val LightColorScheme = DarkColorScheme // Strictly maintain dark theme for full immersion

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Disable dynamic colors to preserve brand cinematic identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme // Force dark cinematic atmosphere

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
