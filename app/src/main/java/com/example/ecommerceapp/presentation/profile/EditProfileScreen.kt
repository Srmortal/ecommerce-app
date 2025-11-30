package com.example.ecommerceapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

val DarkBlueBackground = Color(0xFF0C153B)
val HeaderBackground = Color(0xFF5C63AF)
val DarkerInputBackground = Color(0xFF242A50)
val Blue = Color(0xFF0D5CD1)
val WhiteText = Color(0xFFFFFFFF)
val GrayText = Color(0xFFB0B0B0)
val RedError = Color(0xFFE57373)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    var fullName by rememberSaveable { mutableStateOf(currentUser?.username ?: "") }
    var phone by rememberSaveable { mutableStateOf(currentUser?.phone ?: "") }
    var email by rememberSaveable { mutableStateOf(currentUser?.email ?: "") }
    var shippingAddress by rememberSaveable { mutableStateOf(currentUser?.address ?: "") }
    var validationError by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            fullName = currentUser!!.username
            phone = currentUser!!.phone
            email = currentUser!!.email
            shippingAddress = currentUser!!.address
        }
    }

    val onSaveChanges: () -> Unit = Button@{
        validationError = ""


        if (phone.length < 10) {
            validationError = "Phone must be at least 10 digits."
            return@Button
        }

        val updatedUser = currentUser?.copy(
            phone = phone,
            address = shippingAddress
        )

        if (updatedUser != null) {
            scope.launch {
                val success = viewModel.updateUserDetails(updatedUser)

                if (success) {
                    snackbarHostState.showSnackbar("Profile updated successfully!")
                    navController.popBackStack()
                } else {
                    snackbarHostState.showSnackbar("Failed to update profile. Please try again.")
                }
            }
        }
    }

    Scaffold(
        containerColor = DarkBlueBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            EditProfileAppBar(navController = navController)
        },
        bottomBar = {
            Button(
                onClick = onSaveChanges,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                enabled = !isLoading && !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = WhiteText, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Save Changes",
                        color = WhiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { paddingValues ->
        if (isLoading || currentUser == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = WhiteText)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(24.dp))

                    if (validationError.isNotEmpty()) {
                        Text(
                            text = validationError,
                            color = RedError,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    ProfileInputField(Icons.Default.Person, "Full Name", fullName, { fullName = it }, false)
                    Spacer(modifier = Modifier.height(20.dp))
                    ProfileInputField(Icons.Default.Phone, "Phone", phone, { phone = it }, false)
                    Spacer(modifier = Modifier.height(20.dp))

                    UnchangeableField(
                        icon = Icons.Default.Email,
                        label = "Email Address",
                        value = email,
                        helperText = "Your primary email address cannot be changed."
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileInputField(Icons.Default.Send, "Shipping Address", shippingAddress, { shippingAddress = it }, false)
                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileButtonRow(
                        icon = Icons.Default.Lock,
                        label = "Change Password",
                        onClick = { navController.navigate("change_password") }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun UnchangeableField(icon: ImageVector, label: String, value: String, helperText: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = GrayText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = GrayText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkerInputBackground)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                color = GrayText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = helperText,
            color = GrayText.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Blue,
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
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(color = WhiteText, fontSize = 16.sp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkerInputBackground,
                unfocusedContainerColor = DarkerInputBackground,
                disabledContainerColor = DarkerInputBackground,
                focusedTextColor = WhiteText,
                unfocusedTextColor = WhiteText,
                disabledTextColor = GrayText,
                focusedIndicatorColor = Blue,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Blue,
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun EditProfileHeader( fullName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(HeaderBackground),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Blue.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fullName.firstOrNull()?.toString()?.uppercase() ?: "?",
                    color = WhiteText,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                "Edit Profile",
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

@Composable
fun ProfileButtonRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkerInputBackground)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Blue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = WhiteText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = GrayText,
            modifier = Modifier.size(24.dp)
        )
    }
}