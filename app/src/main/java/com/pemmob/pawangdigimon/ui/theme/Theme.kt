package com.pemmob.pawangdigimon.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CyberNeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00363D),
    onPrimaryContainer = CyberNeonCyan,
    secondary = CyberAmber,
    onSecondary = Color.Black,
    tertiary = CyberElectricBlue,
    background = DarkCyberBackground,
    onBackground = Color(0xFFECEFF1),
    surface = DarkCyberSurface,
    onSurface = Color(0xFFECEFF1),
    surfaceVariant = DarkCyberSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0BEC5),
    outline = DarkCyberOutline,
    outlineVariant = Color(0xFF233547)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006875),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF97F0FF),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF705D00),
    onSecondary = Color.White,
    tertiary = Color(0xFF006399),
    background = LightCyberBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightCyberSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightCyberSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = LightCyberOutline,
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun PawangDigimonTheme(
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
