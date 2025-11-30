package com.example.ecommerceapp.ui.theme

import Logo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommerceapp.R


@Composable
fun AppBar(navController: NavController) {
    Row(
        modifier = Modifier
            .background(color = AppBar)
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Logo(
            size = 40.dp,
            mainColor = MaterialTheme.colorScheme.primary,
            secondColor = MaterialTheme.colorScheme.surface
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.width(130.dp)
        ) {
            IconButton(onClick = { navController.navigate("cart") }) {

                Icon(
                    imageVector = Icons.Default.ShoppingBasket,
                    contentDescription = "Cart",
                    tint = Color(0xFF747794),
                    modifier = Modifier.size(28.dp)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF747794),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    colorFilter = ColorFilter.tint(color = Color(0xFF747794)),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .clickable(onClick = {
                            navController.navigate("profile")
                        })
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF747794),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF747794),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }


        }
    }
}