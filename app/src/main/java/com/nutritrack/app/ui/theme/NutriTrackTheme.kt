package com.nutritrack.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand colors ─────────────────────────────────────────────────
val Green80     = Color(0xFF6BCB77)
val Green40     = Color(0xFF2E7D32)
val GreenDark   = Color(0xFF1B5E20)
val Teal80      = Color(0xFF4DB6AC)
val Teal40      = Color(0xFF00796B)
val Orange80    = Color(0xFFFFB74D)
val Orange40    = Color(0xFFE65100)
val Surface     = Color(0xFFF8FBF8)
val SurfaceDark = Color(0xFF121212)

private val LightColorScheme = lightColorScheme(
    primary          = Green40,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    secondary        = Teal40,
    onSecondary      = Color.White,
    tertiary         = Orange40,
    background       = Surface,
    surface          = Surface,
    onBackground     = Color(0xFF1A1A1A),
    onSurface        = Color(0xFF1A1A1A),
)

private val DarkColorScheme = darkColorScheme(
    primary          = Green80,
    onPrimary        = Color(0xFF003910),
    primaryContainer = GreenDark,
    secondary        = Teal80,
    onSecondary      = Color(0xFF003731),
    tertiary         = Orange80,
    background       = SurfaceDark,
    surface          = Color(0xFF1E1E1E),
    onBackground     = Color(0xFFE6E6E6),
    onSurface        = Color(0xFFE6E6E6),
)

@Composable
fun NutriTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
