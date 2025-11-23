package com.example.ecommerceapp

import AppNavigation
import Logo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import com.example.ecommerceapp.data.local.DataStoreRepository
import com.example.ecommerceapp.presentation.SettingsScreen
import com.example.ecommerceapp.presentation.SignUp
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EcommerceApp)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val dataStoreRepository = remember { DataStoreRepository(context) }
            val systemDarkTheme = isSystemInDarkTheme()
            // Observe dark mode state from DataStore
            val darkModeState by dataStoreRepository.readDarkModeState().collectAsState(initial = systemDarkTheme)
            
            EcommerceAppTheme(darkTheme = darkModeState) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }

                    AnimatedContent(
                        targetState = showSplash,
                        transitionSpec = {
                            if (targetState) {
                                (slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 800)
                                ) + fadeIn(animationSpec = tween(800))) togetherWith
                                        (slideOutHorizontally(
                                            targetOffsetX = { it },
                                            animationSpec = tween(durationMillis = 800)
                                        ) + fadeOut(animationSpec = tween(800)))
                            } else {
                                (slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(durationMillis = 800)
                                ) + fadeIn(animationSpec = tween(800))) togetherWith
                                        (slideOutHorizontally(
                                            targetOffsetX = { -it },
                                            animationSpec = tween(durationMillis = 800)
                                        ) + fadeOut(animationSpec = tween(800)))
                            }
                        },
                        label = "SplashToOtpAnimation"
                    ) { isSplash ->
                        if (isSplash) {
                            AppSplashScreen(onFinished = { showSplash = false })
                        } else {
                            AppNavigation()
                        }
                    }
                }
            }
        }
    }
}
