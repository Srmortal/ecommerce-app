package com.example.ecommerceapp

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.ecommerceapp.data.local.DataStoreRepository
import com.example.ecommerceapp.presentation.landingpage.LandingPage
import com.example.ecommerceapp.presentation.splashscreen.AppSplashScreen
import com.example.ecommerceapp.presentation.splashscreen.SplashScreenViewModel
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EcommerceApp)
        enableEdgeToEdge()

        viewModel = SplashScreenViewModel(DataStoreRepository(applicationContext))

        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemDarkTheme) }
            EcommerceAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val onBoardingCompleted by viewModel.isOnBoardingCompleted().collectAsState(initial = false)
                    var showSplash by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(2000) // Or whatever your splash screen duration is
                        showSplash = false
                    }

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
                        label = "SplashAnimation"
                    ) { isSplash ->
                        if (isSplash) {
                            AppSplashScreen(onFinished = { showSplash = false })
                        } else {
                            if (onBoardingCompleted) {
                                AppNavigation()
                            } else {
                                LandingPage(onOnboardingFinished = { viewModel.saveOnBoardingState(true) })
                            }
                        }
                    }
                }
            }
        }
    }
}