package com.example.ecommerceapp.presentation.change_password

import com.example.ecommerceapp.presentation.profile.DarkBlueBackground
import com.example.ecommerceapp.presentation.profile.HeaderBackground
import com.example.ecommerceapp.presentation.profile.RedError
import com.example.ecommerceapp.presentation.profile.WhiteText
import java.time.format.TextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.viewmodels.ProfileViewModel
import com.example.ecommerceapp.presentation.profile.DarkerInputBackground
import com.example.ecommerceapp.presentation.profile.LightBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var validationError by rememberSaveable { mutableStateOf("") }
    var firebaseError by rememberSaveable { mutableStateOf("") }
    var isChanging by rememberSaveable { mutableStateOf(false) }

    val onChangePassword: () -> Unit = Button@{
        validationError = ""
        firebaseError = ""

        if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            validationError = "All fields must be filled."
            return@Button
        }
        if (newPassword.length < 6) {
            validationError = "New password must be at least 6 characters."
            return@Button
        }
        if (newPassword != confirmPassword) {
            validationError = "New passwords do not match."
            return@Button
        }

        // --- Start Password Change ---
        isChanging = true
        scope.launch {
            val result = viewModel.changePassword(
                currentPassword,
                newPassword
            )
            isChanging = false

            when (result) {
                ProfileViewModel.PasswordChangeResult.SUCCESS -> {
                    snackbarHostState.showSnackbar("Password changed successfully!")
                    navController.popBackStack()
                }
                ProfileViewModel.PasswordChangeResult.INVALID_CREDENTIAL -> {
                    firebaseError = "Current password is incorrect."
                }
                ProfileViewModel.PasswordChangeResult.REQUIRES_RECENT_LOGIN -> {
                    firebaseError = "Please log out and log in again to change your password."
                }
                ProfileViewModel.PasswordChangeResult.GENERIC_ERROR -> {
                    firebaseError = "Failed to change password. Please try again."
                }
            }
        }
    }
    // ----------------------------

    Scaffold(
        containerColor = DarkBlueBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChangePasswordAppBar(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // --- Status Messages ---
                if (validationError.isNotEmpty()) {
                    Text(text = validationError, color = RedError, modifier = Modifier.padding(bottom = 8.dp))
                }
                if (firebaseError.isNotEmpty()) {
                    Text(text = firebaseError, color = RedError, modifier = Modifier.padding(bottom = 8.dp))
                }
                // -----------------------

                // Current Password Input
                PasswordInputField(
                    label = "Current Password",
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(20.dp))

                // New Password Input
                PasswordInputField(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password Input
                PasswordInputField(
                    label = "Confirm New Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    imeAction = ImeAction.Done
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Save Button
                Button(
                    onClick = onChangePassword,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isChanging
                ) {
                    if (isChanging) {
                        CircularProgressIndicator(color = WhiteText, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Change Password",
                            color = WhiteText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = label,
                tint = LightBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = WhiteText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(color = WhiteText, fontSize = 16.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkerInputBackground,
                unfocusedContainerColor = DarkerInputBackground,
                focusedTextColor = WhiteText,
                unfocusedTextColor = WhiteText,
                focusedIndicatorColor = LightBlue,
                unfocusedIndicatorColor = DarkerInputBackground, // Match background for a solid look
                cursorColor = LightBlue,
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                "Change Password",
                color = WhiteText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = WhiteText
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Open menu or settings */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = WhiteText
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = HeaderBackground)
    )
}