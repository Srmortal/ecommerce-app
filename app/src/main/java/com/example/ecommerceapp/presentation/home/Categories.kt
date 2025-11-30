package com.example.ecommerceapp.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.presentation.AccentColor

data class Category(
    val title: String,
    val icon: ImageVector? = null,
    val imageUrl: String? = null,
    val isHighlight: Boolean = false
)

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor =
        if (isSelected) Color(0xFF4A3575) else if (category.isHighlight) Color(0xFF4A3575) else Color(
            0xFF242644
        )
    val contentColor =
        if (isSelected) AccentColor else if (category.isHighlight) AccentColor else Color(
            0xFF747794
        )
    val titleColor =
        if (isSelected) AccentColor else if (category.isHighlight) AccentColor else Color.White
    val modifierWithBorder = if (isSelected) Modifier.border(
        BorderStroke(1.dp, AccentColor),
        RoundedCornerShape(12.dp)
    ) else Modifier

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(modifierWithBorder)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (category.imageUrl != null) {
            coil.compose.AsyncImage(
                model = category.imageUrl,
                contentDescription = category.title,
                modifier = Modifier.size(32.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(contentColor)
            )
        } else if (category.icon != null) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.title,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
            color = titleColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}