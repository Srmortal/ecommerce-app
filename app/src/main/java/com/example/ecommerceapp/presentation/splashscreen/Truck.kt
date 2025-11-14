package com.example.ecommerceapp.presentation.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R

@Composable
fun Truck() {
    Image(
        painter = painterResource(R.drawable.truck),
        contentDescription = "Truck",
        modifier = Modifier.offset(x = 15.dp, y = (-15).dp)
    )
}