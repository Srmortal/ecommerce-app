package com.example.ecommerceapp.presentation.signin

import InputField
import Logo
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import com.example.ecommerceapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommerceapp.data.Constants
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme // <-- IMPORTANT: Make sure this import exists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun ForgetPassword(navController: NavController, innerPadding: PaddingValues) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    val auth: FirebaseAuth? = if (!LocalInspectionMode.current) {
        FirebaseAuth.getInstance()
    } else {
        null
    }

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
        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            label = "Email Address",
            text = email,
            onTextChange = { email = it },
            placeholder = "info@example.com",
            modifier = Modifier,
            icon = R.drawable.baseline_person_outline_24
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (auth == null) {
                    Toast.makeText(context, "Preview Mode: Button disabled.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                Log.d("ResetClicked", "Reset button clicked: ")

                if (email.isBlank()) {
                    errorMessage = "Email cannot be empty"
                    return@Button
                }

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Password reset email sent.", Toast.LENGTH_LONG).show()
                            navController.navigate("signin") {
                                popUpTo("signin") {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }                        } else {
                            when (task.exception) {
                                is FirebaseAuthInvalidUserException ->
                                    errorMessage = "No account found with this email"

                                else ->
                                    errorMessage =
                                        task.exception?.localizedMessage ?: "Authentication failed"
                            }
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text("Send Reset Email", style = MaterialTheme.typography.displayMedium)        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewForgetPassword() {
    EcommerceAppTheme {
        ForgetPassword(
            navController = NavController(LocalContext.current),
            innerPadding = PaddingValues(0.dp)
        )
    }
}
