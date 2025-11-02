package com.example.ecommerceapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.data.Constants

@Composable
fun OTP(innerPadding: PaddingValues) {
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(innerPadding)
            .padding(Constants.screenPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            "Email Verification",
            color = Color.White,
            fontWeight = FontWeight.W600,
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "We will send you an OTP on this email",
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            value = email,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0x4DFFFFFF),
//                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            )
        ) { Text("Send OTP", color = Color.Black, fontWeight = FontWeight.W900) }
        Row {
            Text("By providing my email, I hereby agree the ", color = Color.White)
            Text("Term of Services ", fontWeight = FontWeight.W900, color = Color.White)
            Text("and ", fontWeight = FontWeight.W900, color = Color.White)
            Text("Privacy Policy. ", fontWeight = FontWeight.W900, color = Color.White)
        }
    }
}