package com.example.ecommerceapp

import ErrorText
import InfoText
import InputField
import Logo
import android.util.Log
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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.ecommerceapp.data.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun SignIn(navController: NavController, innerPadding: PaddingValues) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isPressed by remember { mutableStateOf(false) }
    LocalContext.current

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

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

        if (errorMessage.isNotEmpty()) {
            ErrorText(errorMessage)
        }

        InputField(
            label = "Email Address",
            text = email,
            onTextChange = { email = it },
            placeholder = "info@example.com",
            modifier = Modifier,
            icon = R.drawable.at
        )
        InputField(
            label = "Password",
            text = password,
            onTextChange = { password = it },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true,
            modifier = Modifier
        )

        TextButton(
            onClick = {
                Log.d("LoginClicked", "SignIn: ")

                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Email and Password cannot be empty"
                    return@TextButton
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            errorMessage = ""
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            when (task.exception) {
                                is FirebaseAuthInvalidUserException ->
                                    errorMessage = "No account found with this email"

                                is FirebaseAuthInvalidCredentialsException ->
                                    errorMessage = "Incorrect password"

                                else ->
                                    errorMessage =
                                        task.exception?.localizedMessage ?: "Authentication failed"
                            }
                        }
                    }
            },
            colors = ButtonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text("Login", color = Color.Black, style = MaterialTheme.typography.displayMedium)
        }

        TextButton(
            {
                Log.d("ForgetClicked", "SignIn: ")
            },
        ) {
            Text(
                "Forget Password?",
                color = if (isPressed) MaterialTheme.colorScheme.secondary else Color.White,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            navController.navigate("forgetpassword")
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
                "register" to { navController.navigate("signup") },
            )
        )
    }

}
