package com.example.ecommerceapp.presentation.splashscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.ui.theme.Truck


@Composable
fun AnimatedRoadWithTruck() {
    val offsetX = remember { Animatable(0f) }
    val roadWidth = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        roadWidth.animateTo(
            targetValue = 300f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )

        while (true) {
            offsetX.animateTo(
                targetValue = -100f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )
            offsetX.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .offset(y = 180.dp)
            .height(50.dp)
            .width(roadWidth.value.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(30.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF2C2C2C))
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { translationX = offsetX.value }
            ) {
                val dashWidth = 30.dp.toPx()
                val dashHeight = 4.dp.toPx()
                val spacing = 20.dp.toPx()

                var x = -dashWidth
                while (x < size.width + dashWidth) {
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(x, size.height / 2 - dashHeight / 2),
                        size = Size(dashWidth, dashHeight),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                    x += dashWidth + spacing
                }
            }
        }

        Truck()
    }
}