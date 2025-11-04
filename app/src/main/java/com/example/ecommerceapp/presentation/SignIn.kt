package com.example.ecommerceapp

import ErrorText
import InfoText
import InputField
import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.data.Constants

@Composable
fun SignIn(innerPadding: PaddingValues) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPressed by remember { mutableStateOf(false) }

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
        Logo(100.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
        Text("Suha", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Box(modifier = Modifier.padding(vertical = 20.dp))
        ErrorText("Incorrect Email Address or Password")
        InputField(
            label = "Email Address",
            text = email,
            onTextChange = { email = it },
            placeholder = "info@example.com",
            icon = R.drawable.at
        )
        InputField(
            label = "Password",
            text = password,
            onTextChange = { password = it },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true
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
        ) { Text("Login", color = Color.Black, style = MaterialTheme.typography.displayMedium) }
        TextButton(
            {},
        ) {
            Text(
                "Forget Password?",
                color = if (isPressed) MaterialTheme.colorScheme.secondary else Color.White,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true

                            try {
                                awaitRelease()
                            } finally {
                                isPressed = false
                            }
                        },
                    )
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
        InfoText(
            strings = listOf(
                "Don't have an account? ", "Register Now"
            ),
            tags = listOf("register"),
            onTagClick = mapOf(
                "register" to { println("Register clicked") },
            )
        )
    }
}
