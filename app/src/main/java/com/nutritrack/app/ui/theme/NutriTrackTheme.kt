package com.nutritrack.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand colors ─────────────────────────────────────────────────
val Blue80      = Color(0xFF64B5F6)   // light blue
val Blue40      = Color(0xFF1565C0)   // primary blue
val BlueDark    = Color(0xFF0D47A1)   // dark blue
val Teal80      = Color(0xFF4DD0E1)   // accent teal
val Teal40      = Color(0xFF00838F)   // accent teal dark

// ── Brand colors ─────────────────────────────────────────────────
//val Blue80     = Color(0xFF6BCB77)
//val Blue40     = Color(0xFF2E7D32)
//val BlueDark   = Color(0xFF1B5E20)
//val Teal80      = Color(0xFF4DB6AC)
//val Teal40      = Color(0xFF00796B)
val Orange80    = Color(0xFFFFB74D)
val Orange40    = Color(0xFFE65100)
val Surface     = Color(0xFFF8FBF8)
val SurfaceDark = Color(0xFF121212)

private val LightColorScheme = lightColorScheme(
    primary          = Blue40,
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
    primary          = Blue80,
    onPrimary        = Color(0xFF003910),
    primaryContainer = BlueDark,
    secondary        = Teal80,
    onSecondary      = Color(0xFF003731),
    tertiary         = Orange80,
    background       = SurfaceDark,
    surface          = Color(0xFF1E1E1E),
    onBackground     = Color(0xFFE6E6E6),
    onSurface        = Color(0xFF9C9595),
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
