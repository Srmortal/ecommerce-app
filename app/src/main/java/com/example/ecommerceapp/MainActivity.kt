package com.example.ecommerceapp

import Logo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.data.Remote.DataUploader
import com.example.ecommerceapp.presentation.Home
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EcommerceApp); enableEdgeToEdge()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("MainActivity", "Triggering one-time data upload...")
            DataUploader.fetchAndUploadProducts()
            DataUploader.fetchAndUploadCategories()
        }
        setContent {
            EcommerceAppTheme {
                var showSplash = remember { mutableStateOf(true) }

                if (showSplash.value) {
                    AppSplashScreen(
                        onFinished = {
                            showSplash.value = false
                        }
                    )
                } else {
                    Scaffold(modifier = Modifier
                        .fillMaxSize()
                    ) { innerPadding ->
//                            SignIn(innerPadding)
//                            SignUp(innerPadding)
//                        OTP(innerPadding)
//                            VerifyOTP(innerPadding)
                        Home(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun AppSplashScreen(onFinished: () -> Unit) {
    // Animations
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900)
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        delay(800)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Logo(
            size = (200.dp * scale.value),
            mainColor = MaterialTheme.colorScheme.secondary,
            secondColor = MaterialTheme.colorScheme.onSecondary
        )

        Modifier.alpha(alpha.value)
    }
}
