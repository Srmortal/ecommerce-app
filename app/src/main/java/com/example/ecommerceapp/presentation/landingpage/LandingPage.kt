package com.example.ecommerceapp.presentation.landingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R

@Preview(showBackground = true, name = "Landing Page Preview")
@Composable
fun LandingPagePreview() {
    val previewScreenData = listOf(
        ScreenData(android.R.drawable.ic_menu_gallery, "Preview Title", "Preview description text.")
    )
    LandingPageContent(screens = previewScreenData, initialScreen = 0, onOnboardingFinished = {})
}

@Composable
fun LandingPage(onOnboardingFinished: () -> Unit) {
    val screens = listOf(
        ScreenData(
            imageRes = R.drawable.landingpage1,
            title = "Find Items What You Are Looking For",
            description = "Various available goods you need."
        ),
        ScreenData(
            imageRes = R.drawable.landingpage2,
            title = "Add Quantity Selected Items",
            description = "You can add the number of items."
        ),
        // New screen data added
        ScreenData(
            imageRes = R.drawable.landingpage3,
            title = "Save Items Into Basket",
            description = "Can save items in your cart."
        )
    )
    LandingPageContent(screens = screens, initialScreen = 0, onOnboardingFinished = onOnboardingFinished)
}

@Composable
fun LandingPageContent(screens: List<ScreenData>, initialScreen: Int, onOnboardingFinished: () -> Unit) {
    var currentScreen by remember { mutableStateOf(initialScreen) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(screens[currentScreen].imageRes),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = screens[currentScreen].title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333477)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = screens[currentScreen].description,
            fontSize = 14.sp,
            color = Color(0xFF818181)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Dots Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (currentScreen == index) 12.dp else 8.dp)
                        .background(
                            color = if (currentScreen == index) Color(
                                0xFF333477
                            ) else Color.LightGray,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Next Button
        Button(
            onClick = {
                if (currentScreen < screens.lastIndex) {
                    currentScreen++
                } else {
                    onOnboardingFinished()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333477)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (currentScreen == screens.lastIndex) "Start" else "Next", color = Color.White)
        }
    }
}

data class ScreenData(val imageRes: Int, val title: String, val description: String)