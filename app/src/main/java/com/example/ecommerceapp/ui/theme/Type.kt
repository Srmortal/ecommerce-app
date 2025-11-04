package com.example.ecommerceapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R


private val Nunito = FontFamily(
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold)
)
val Typography = Typography(                           /* Large Text ( Suha, etc... ) */
    headlineMedium = TextStyle(
        fontFamily = Nunito,
        fontSize = 32.sp,
        fontWeight = FontWeight.ExtraBold
    ),
    titleMedium = TextStyle(                          /* ( Email Verification, etc )  */
        fontFamily = Nunito,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    displayMedium = TextStyle(                        /* Buttons */
        fontFamily = Nunito,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyMedium = TextStyle(                          /* Normal Text */
        fontFamily = Nunito,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(                          /* Small Text */
        fontFamily = Nunito,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light
    ),
)