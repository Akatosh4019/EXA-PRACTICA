package pe.edu.upeu.bibliomobil.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF2457A7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E2FF),
    onPrimaryContainer = Color(0xFF001A41),
    inversePrimary = Color(0xFFAFC6FF),
    secondary = Color(0xFF765A00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDEA1),
    onSecondaryContainer = Color(0xFF251A00),
    tertiary = Color(0xFF65558F),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEADDFF),
    onTertiaryContainer = Color(0xFF201047),
    background = Color(0xFFF9F9FF),
    onBackground = Color(0xFF1A1B20),
    surface = Color(0xFFF9F9FF),
    onSurface = Color(0xFF1A1B20),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44464F),
    surfaceTint = Color(0xFF2457A7),
    inverseSurface = Color(0xFF2F3036),
    inverseOnSurface = Color(0xFFF1F0F7),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF757780),
    outlineVariant = Color(0xFFC5C6D0),
    scrim = Color.Black,
    surfaceBright = Color(0xFFF9F9FF),
    surfaceDim = Color(0xFFDAD9E0),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF3F3FA),
    surfaceContainer = Color(0xFFEEEEF4),
    surfaceContainerHigh = Color(0xFFE8E7EE),
    surfaceContainerHighest = Color(0xFFE2E2E9),
    primaryFixed = Color(0xFFD9E2FF),
    primaryFixedDim = Color(0xFFAFC6FF),
    onPrimaryFixed = Color(0xFF001A41),
    onPrimaryFixedVariant = Color(0xFF003F7F),
    secondaryFixed = Color(0xFFFFDEA1),
    secondaryFixedDim = Color(0xFFE9C349),
    onSecondaryFixed = Color(0xFF251A00),
    onSecondaryFixedVariant = Color(0xFF594400),
    tertiaryFixed = Color(0xFFEADDFF),
    tertiaryFixedDim = Color(0xFFD0BCFF),
    onTertiaryFixed = Color(0xFF201047),
    onTertiaryFixedVariant = Color(0xFF4D3D75),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFAFC6FF),
    onPrimary = Color(0xFF002E68),
    primaryContainer = Color(0xFF003F7F),
    onPrimaryContainer = Color(0xFFD9E2FF),
    inversePrimary = Color(0xFF2457A7),
    secondary = Color(0xFFE9C349),
    onSecondary = Color(0xFF3D2F00),
    secondaryContainer = Color(0xFF594400),
    onSecondaryContainer = Color(0xFFFFDEA1),
    tertiary = Color(0xFFD0BCFF),
    onTertiary = Color(0xFF36265D),
    tertiaryContainer = Color(0xFF4D3D75),
    onTertiaryContainer = Color(0xFFEADDFF),
    background = Color(0xFF111318),
    onBackground = Color(0xFFE2E2E9),
    surface = Color(0xFF111318),
    onSurface = Color(0xFFE2E2E9),
    surfaceVariant = Color(0xFF44464F),
    onSurfaceVariant = Color(0xFFC5C6D0),
    surfaceTint = Color(0xFFAFC6FF),
    inverseSurface = Color(0xFFE2E2E9),
    inverseOnSurface = Color(0xFF2F3036),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF8F909A),
    outlineVariant = Color(0xFF44464F),
    scrim = Color.Black,
    surfaceBright = Color(0xFF37393E),
    surfaceDim = Color(0xFF111318),
    surfaceContainerLowest = Color(0xFF0C0E13),
    surfaceContainerLow = Color(0xFF1A1B20),
    surfaceContainer = Color(0xFF1E1F25),
    surfaceContainerHigh = Color(0xFF282A2F),
    surfaceContainerHighest = Color(0xFF33343A),
    primaryFixed = Color(0xFFD9E2FF),
    primaryFixedDim = Color(0xFFAFC6FF),
    onPrimaryFixed = Color(0xFF001A41),
    onPrimaryFixedVariant = Color(0xFF003F7F),
    secondaryFixed = Color(0xFFFFDEA1),
    secondaryFixedDim = Color(0xFFE9C349),
    onSecondaryFixed = Color(0xFF251A00),
    onSecondaryFixedVariant = Color(0xFF594400),
    tertiaryFixed = Color(0xFFEADDFF),
    tertiaryFixedDim = Color(0xFFD0BCFF),
    onTertiaryFixed = Color(0xFF201047),
    onTertiaryFixedVariant = Color(0xFF4D3D75),
)

private val AppTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp)
)

private val AppShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
)

@Composable
fun BiblioMobilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
