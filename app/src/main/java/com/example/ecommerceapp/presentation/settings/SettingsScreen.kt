package com.example.ecommerceapp.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        containerColor = DarkBlueBackground,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Account",
                    color = GrayText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    trailingText = "View",
                    onClick = { navController.navigate("profile") }
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Profile",
                    trailingText = "Edit",
                    onClick = { navController.navigate("edit_profile") }
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.ShoppingCart,
                    label = "Orders",
                    trailingText = "History",
                    onClick = { navController.navigate("orders") }
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.AddShoppingCart,
                    label = "Cart",
                    trailingText = "View",
                    onClick = { navController.navigate("cart") }
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.FavoriteBorder,
                    label = "Wishlist",
                    trailingText = "View",
                    onClick = { navController.navigate("wishlist") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Security",
                    color = GrayText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    label = "Password",
                    trailingText = "Change",
                    onClick = { navController.navigate("change_password") }
                )
            }
            item {
                LogoutButton(
                    onClick = {
                        auth.signOut()
                        navController.navigate("signin") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkerInputBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = LightBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    color = WhiteText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                trailingText?.let {
                    Text(
                        text = it,
                        color = GrayText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkerInputBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = RedError, // Red color for logout
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Logout",
                color = RedError,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}