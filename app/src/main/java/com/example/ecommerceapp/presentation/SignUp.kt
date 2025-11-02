package com.example.ecommerceapp.presentation

import InputField
import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(innerPadding)
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Text("Suha", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.W900)
        Box(modifier = Modifier.padding(vertical = 20.dp))
        InputField(
            label = "Email",
            text = email,
            onTextChange = { email = it },
            placeholder = "info@example.com",
            icon = R.drawable.at
        )

        InputField(
            label = "Username",
            text = username,
            onTextChange = { username = it },
            placeholder = "Mohamed Ali",
            icon = R.drawable.profile
        )

        InputField(
            label = "Password",
            text = password,
            onTextChange = { password = it },
            placeholder = "Enter Password",
            icon = R.drawable.key,
            isPassword = true
        )

        InputField(
            label = "Repeat Password",
            text = repeatPassword,
            onTextChange = { repeatPassword = it },
            placeholder = "Re-enter Password",
            icon = R.drawable.key,
            isPassword = true
        )
        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.screenPadding)
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            )
        ) { Text("Sign Up", color = Color.Black) }
        Row {
            Text(
                "Already have an account? ",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.W300
            )
            Text(
                "Sign In",
                modifier = Modifier.clickable {},
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.W900,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
