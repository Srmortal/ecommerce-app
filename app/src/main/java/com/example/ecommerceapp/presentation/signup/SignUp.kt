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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // Import Firestore
import kotlinx.coroutines.launch

@Composable
fun SignUp(navController: NavController, innerPadding: PaddingValues) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val emailFocus = remember { FocusRequester() }
    val usernameFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val repeatPasswordFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val addressFocus = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var repeatPasswordError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }

    var generalMessage by remember { mutableStateOf("") }

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
                generalMessage = ""
            },
            placeholder = "info@example.com",
            icon = R.drawable.at,
            imeAction = ImeAction.Next,
            onNext = { usernameFocus.requestFocus() },
            isPassword = false,
            modifier = Modifier.focusRequester(emailFocus)
        )
        if (emailError.isNotEmpty()) ErrorText(emailError)

        InputField(
            label = "Username",
            text = username,
            onTextChange = {
                username = it
                usernameError = ""
                generalMessage = ""
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
                generalMessage = ""
            },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true,
            keyboardType = KeyboardType.Password,
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
                generalMessage = ""
            },
            placeholder = "******",
            icon = R.drawable.key,
            isPassword = true,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password,
            onNext = { phoneFocus.requestFocus() },
            modifier = Modifier.focusRequester(repeatPasswordFocus)
        )
        if (repeatPasswordError.isNotEmpty()) ErrorText(repeatPasswordError)

        InputField(
            label = "Phone Number",
            text = phone,
            onTextChange = {
                phone = it
                phoneError = ""
                generalMessage = ""
            },
            placeholder = "e.g., +1234567890",
            icon = R.drawable.mobile,
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Phone,
            onNext = { addressFocus.requestFocus() },
            modifier = Modifier.focusRequester(phoneFocus)
        )
        if (phoneError.isNotEmpty()) ErrorText(phoneError)

        InputField(
            label = "Full Address",
            text = address,
            onTextChange = {
                address = it
                addressError = ""
                generalMessage = ""
            },
            placeholder = "Street, City, Postal Code",
            icon = R.drawable.location,
            imeAction = ImeAction.Done,
            onNext = { /* Done */ },
            modifier = Modifier.focusRequester(addressFocus)
        )
        if (addressError.isNotEmpty()) ErrorText(addressError)

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                emailError = ""
                usernameError = ""
                passwordError = ""
                repeatPasswordError = ""
                phoneError = ""
                addressError = ""
                generalMessage = ""
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
                if (phone.isBlank() || phone.length < 10) { // Simple phone validation
                    phoneError = "Please enter a valid phone number (min 10 digits)"
                    hasError = true
                }
                if (address.isBlank()) {
                    addressError = "Address cannot be empty"
                    hasError = true
                }

                if (hasError) return@Button

                isLoading = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uid = user?.uid

                            if (user != null && uid != null) {
                                val userDetails = hashMapOf(
                                    "uid" to uid,
                                    "email" to email,
                                    "username" to username,
                                    "phone" to phone,
                                    "address" to address,
                                    "createdAt" to com.google.firebase.Timestamp.now()
                                )

                                firestore.collection("users").document(uid).set(userDetails)
                                    .addOnSuccessListener {
                                        user.sendEmailVerification()
                                            .addOnCompleteListener { verifyTask ->
                                                isLoading = false
                                                if (verifyTask.isSuccessful) {
                                                    otpSent = true
                                                    isSuccess = true
                                                    generalMessage = "Registration successful! Verification email sent."
                                                    auth.signOut()
                                                } else {
                                                    generalMessage = verifyTask.exception?.message
                                                        ?: "Failed to send verification email. Details saved."
                                                }
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        isLoading = false
                                        generalMessage = "Registration successful, but failed to save details: ${e.message}"
                                    }
                            } else {
                                isLoading = false
                                generalMessage = "Registration failed: User created but UID not found."
                            }
                        } else {
                            isLoading = false
                            generalMessage = task.exception?.message ?: "Registration failed."
                        }
                    }

            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(5.dp),
            enabled = !isLoading

        ) {
            if (isLoading) CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            ) else Text("Sign Up")
        }

        if (generalMessage.isNotEmpty()) {
            Text(
                generalMessage,
                color = if (isSuccess) Color.Green else Color.Red,
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