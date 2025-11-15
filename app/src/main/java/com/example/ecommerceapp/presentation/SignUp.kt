package com.example.ecommerceapp.presentation

import ErrorText
import InfoText
import InputField
import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun SignUp(navController: NavController, innerPadding: PaddingValues) {
    val auth = FirebaseAuth.getInstance()

    val usernameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val repeatPasswordFocus = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var repeatPasswordError by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var otpSent by remember { mutableStateOf(false) }

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
        Spacer(Modifier.height(20.dp))

        InputField(
            label = "Email Address",
            text = email,
            onTextChange = {
                email = it
                emailError = ""
            },
            placeholder = "info@example.com",
            icon = R.drawable.at,
            imeAction = ImeAction.Next,
            onNext = { usernameFocus.requestFocus() },
            modifier = Modifier.focusRequester(usernameFocus)
        )
        if (emailError.isNotEmpty()) ErrorText(emailError)

        InputField(
            label = "Username",
            text = username,
            onTextChange = {
                username = it
                usernameError = ""
            },
            placeholder = "Mohamed Ali",
            icon = R.drawable.profile,
            imeAction = ImeAction.Next,
            onNext = { passwordFocus.requestFocus() },
            modifier = Modifier.focusRequester(usernameFocus)
        )
        if (usernameError.isNotEmpty()) ErrorText(usernameError)

        InputField(
            label = "Password",
            text = password,
            onTextChange = {
                password = it
                passwordError = ""
            },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true,
            imeAction = ImeAction.Next,
            onNext = { repeatPasswordFocus.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocus)
        )
        if (passwordError.isNotEmpty()) ErrorText(passwordError)

        InputField(
            label = "Repeat Password",
            text = repeatPassword,
            onTextChange = {
                repeatPassword = it
                repeatPasswordError = ""
            },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true,
            imeAction = ImeAction.Done,
            onNext = {  },
            modifier = Modifier.focusRequester(repeatPasswordFocus)
        )
        if (repeatPasswordError.isNotEmpty()) ErrorText(repeatPasswordError)

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                emailError = ""
                usernameError = ""
                passwordError = ""
                repeatPasswordError = ""
                isSuccess = false
                otpSent = false

                var hasError = false
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Invalid email format"
                    hasError = true
                }
                if (username.length < 4) {
                    usernameError = "Username must be at least 4 characters"
                    hasError = true
                }
                if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    hasError = true
                }
                if (password != repeatPassword) {
                    repeatPasswordError = "Passwords do not match"
                    hasError = true
                }

                if (hasError) return@Button

                isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {
                                            otpSent = true
                                            isSuccess = true
                                        } else {
                                            emailError = verifyTask.exception?.message
                                                ?: "Failed to send verification email"
                                        }
                                    }
                            } else {
                                emailError = task.exception?.message
                                    ?: "Registration failed"
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
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(5.dp)

        ) {
            if (isLoading) CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            ) else Text("Sign Up")
        }

        if (otpSent) {
            Text(
                "Verification email sent! Please check your inbox.",
                color = Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        InfoText(
            listOf("Already have an account? ", "Login"),
            listOf("login"),
            mapOf("login" to { navController.navigate("signin") })
        )
    }
}
