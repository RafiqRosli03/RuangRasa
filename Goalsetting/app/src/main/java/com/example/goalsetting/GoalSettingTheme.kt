package com.example.goalsetting.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GoalSettingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private fun lightColorScheme() = androidx.compose.material3.lightColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    secondary = Teal200,
    onSecondary = Color.White,
    tertiary = Purple700,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

val Purple500 = Color(0xFF6200EE)
val Teal200 = Color(0xFF03DAC5)
val Purple700 = Color(0xFF3700B3)

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
