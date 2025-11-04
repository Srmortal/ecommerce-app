package com.example.ecommerceapp.presentation

import ErrorText
import InfoText
import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
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
                        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(innerPadding)
            .padding(Constants.screenPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackwardIcon(Modifier.align(alignment = Alignment.Start))

        Spacer(modifier = Modifier.size(24.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Logo(100.dp, MaterialTheme.colorScheme.secondary)
            Text(
                "Suha", color = Color.White, style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            "Email Verification",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Text(
            "We will send you an OTP on this email.",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium
        )

        EmailInput(email = email, { email = it })


        ErrorText("Invalid email format. Make sure your address looks like info@example.com")
        val interactionSource = remember { MutableInteractionSource() }
        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(5.dp),
                )
                .padding(vertical = 10.dp),

//                .indication(
//                    interactionSource,
//                    ripple(
//                        color = Color(0xFFFFD500),
//                        bounded = true
//                    )
//            contentPadding = PaddingValues(13.dp),

            interactionSource = interactionSource,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Black, containerColor = MaterialTheme.colorScheme.secondary
            ),

            ) {
            Text("Send OTP", style = MaterialTheme.typography.displayMedium)
        }
//        PolicyText()
        InfoText(
            strings = listOf(
                "By providing my email, I hereby agree the ",
                "Term of Services",
                " and ",
                "Privacy Policy."
            ),
            tags = listOf("register", "login"),
            onTagClick = mapOf(
                "term of services" to { println("Register clicked") },
                "privacy policy" to { println("Login clicked") }
            )
        )
    }
}

@Composable
fun BackwardIcon(modifier: Modifier = Modifier) {
    var isPressed by remember { mutableStateOf(false) }

    Icon(
        painter = painterResource(R.drawable.backward_arrow),
        tint = if (isPressed) MaterialTheme.colorScheme.secondary else Color.White,
        modifier = modifier
            .size(50.dp)
            .pointerInput(Unit) {
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
        contentDescription = "Back Button"
    )
}

@Composable
fun EmailInput(email: String, onEmailChange: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = { Text("info@example.com") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.at),
                modifier = Modifier.size(18.dp),
                contentDescription = "Email Icon",
                tint = Color.White
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0x4DFFFFFF),
            focusedContainerColor = Color(0x4DFFFFFF),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}