package com.example.ecommerceapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.ecommerceapp.presentation.SignUp
import org.junit.Rule
import org.junit.Test

class SignUpTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testValidEmail() {
        composeTestRule.setContent {
            SignUp(innerPadding = PaddingValues())
        }
        composeTestRule
            .onNodeWithText("Email Address")
            .assertExists()
            .performTextInput("william.strong@my-own-personal-domain.com")
        composeTestRule
            .onNodeWithText("Sign Up")
            .performClick()
    }
}