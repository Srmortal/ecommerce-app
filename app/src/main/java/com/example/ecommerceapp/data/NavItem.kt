package com.example.ecommerceapp.data

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String? = null,
    val onClick: () -> Unit
)
