package com.example.ecommerceapp.presentation

import ErrorText
import InfoText
import InputField
import Logo
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Constants

@Composable
fun SignUp(innerPadding: PaddingValues) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
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
        Logo(100.dp, MaterialTheme.colorScheme.secondary)
        Text("Suha", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        Box(modifier = Modifier.padding(vertical = 20.dp))
        InputField(
            label = "Email Address",
            text = email,
            onTextChange = { email = it },
            placeholder = "info@example.com",
            icon = R.drawable.at
        )
        ErrorText("Invalid email format. Make sure your address looks like info@example.com")
        InputField(
            label = "Username",
            text = username,
            onTextChange = { username = it },
            placeholder = "Mohamed Ali",
            icon = R.drawable.profile
        )
        ErrorText("Invalid Username format. Make sure your username have at least 4 characters")
        InputField(
            label = "Password",
            text = password,
            onTextChange = { password = it },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true
        )
        ErrorText("Invalid Password format. Make sure your password have at least 6 characters")
        InputField(
            label = "Repeat Password",
            text = repeatPassword,
            onTextChange = { repeatPassword = it },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true
        )
        ErrorText("Repeat password doesn't match the password field")
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
        ) { Text("Sign Up", color = Color.Black, style = MaterialTheme.typography.displayMedium) }
        InfoText(
            listOf("Already have an account? ", "Login"),
            listOf("login"),
            mapOf("login" to { println("Login clicked") })
        )
    }
}