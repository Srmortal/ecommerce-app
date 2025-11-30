package com.example.ecommerceapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.models.User
import com.example.ecommerceapp.data.viewmodels.ProfileViewModel

val LightPurpleCard = Color(0xFF333B65)
val LightBlue = Color(0xFF0D5CD1)

@Composable
fun ProfileScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: ProfileViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize().background(color = DarkBlueBackground)
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = WhiteText)
            }
        } else if (currentUser != null) {
            ProfileDetailsCard(currentUser!!)
        } else {
            Text("User data unavailable.", color = Color.Red, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("edit_profile") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = WhiteText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Edit Profile",
                color = WhiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileDetailsCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightPurpleCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileDetailRow(Icons.Default.Person, "Full Name", user.username)
            Divider(color = DarkBlueBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileDetailRow(Icons.Default.Phone, "Phone", user.phone)
            Divider(color = DarkBlueBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileDetailRow(Icons.Default.Email, "Email", user.email)
            Divider(color = DarkBlueBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileDetailRow(Icons.Default.LocalShipping, "Shipping:", user.address)
            Divider(color = DarkBlueBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            ProfileOrdersRow(Icons.Default.Star, "My Orders")
        }
    }
}

@Composable
fun ProfileDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = GrayText, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = GrayText, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = value, color = WhiteText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileOrdersRow(icon: ImageVector, label: String) {
    // ... (No change)
}