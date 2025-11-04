package com.example.ecommerceapp.presentation

import ErrorText
import InfoText
import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.input.key.Key
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.data.Constants

@Composable
fun VerifyOTP(innerPadding: PaddingValues) {
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
            "Enter the OTP code that has been sent to info@exmaple.com",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium
        )

        ErrorText("Invalid OTP code provided")
        OtpInput(onOtpComplete = { println("OTP: $it") })
        val interactionSource = remember { MutableInteractionSource() }
        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(5.dp),
                )
                .padding(vertical = 10.dp),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Black, containerColor = MaterialTheme.colorScheme.secondary
            ),

            ) {
            Text("Verify & Proceed", style = MaterialTheme.typography.displayMedium)
        }
        InfoText(
            strings = listOf(
                "Don't received the OTP? ",
                "Resend",
            ),
            tags = listOf("otp"),
            onTagClick = mapOf(
                "otp" to { println("otp clicked") },
            )
        )
    }
}


@Composable
fun OtpInput(
    otpLength: Int = 4,
    onOtpComplete: (String) -> Unit
) {
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }


    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(otpLength) { index ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x55FFFFFF))
                    .clickable { focusRequesters[index].requestFocus() },
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = otpValues[index],
                    onValueChange = { value ->
                        if (value.length <= 1 && value.all(Char::isDigit)) {
                            otpValues[index] = value

                            if (value.isNotEmpty() && index < otpLength - 1)
                                focusRequesters[index + 1].requestFocus()

                            if (otpValues.joinToString("").length == otpLength)
                                onOtpComplete(otpValues.joinToString(""))
                        }
                    },
                    modifier = Modifier
                        .width(20.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent {
                            if (it.key == Key.Backspace && otpValues[index].isEmpty() && index > 0) {
                                otpValues[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                            }
                            false
                        },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}
