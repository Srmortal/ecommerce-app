package com.example.ecommerceapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommerceapp.data.NavItem

@Composable
fun BottomBar(navController: NavController, currentRoute: String?) {
    val navItems = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            onClick = { navController.navigate("home") }
        ),
        NavItem(
            label = "Cart",
            icon = Icons.Outlined.ShoppingCart,
            onClick = { navController.navigate("cart") }
        ),
        NavItem(
            label = "Wishlist",
            icon = Icons.Outlined.FavoriteBorder,
            onClick = { navController.navigate("wishlist") }
        ),
        NavItem(
            label = "Settings",
            icon = Icons.Outlined.Settings,
            onClick = { navController.navigate("settings") }
        ),
    )

    val selectedIndex = when (currentRoute) {
        "home" -> 0
        "cart" -> 1
        "wishlist" -> 2
        "settings" -> 3
        else -> 0
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(AppBar),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .clickable { item.onClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (index == selectedIndex) Color.White else Color.Gray
                )
                Text(
                    text = item.label,
                    color = if (index == selectedIndex) Color.White else Color.Gray,
                    fontSize = 11.sp,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}