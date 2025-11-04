package com.example.ecommerceapp.ui.theme

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
    primary = RoyalBlue,
    secondary = Orange,
    onSecondary = LightOrange,
    secondaryContainer = MidOrange,
    onSecondaryContainer = Color.Black,
    error = Amber,
    background = ElectricIndigo
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue,
    secondary = Orange,
    secondaryContainer = MidOrange,
    onSecondaryContainer = Color.Black,
    onSecondary = LightOrange,
    error = Amber,
    background = ElectricIndigo
)



@Composable
fun EcommerceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}