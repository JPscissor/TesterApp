package ru.jpscissor.frprototype.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xff383838),
    onBackground = Color(0xff4C4C4C),
    surface = Color(0xffC8C7C7),
    onSurface = Color(0xffDBDBDB),
    tertiary = Color(0xffFFFFFF)
)


@Composable
fun FRprototypeTheme(
    content: @Composable () -> Unit
) {

    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}