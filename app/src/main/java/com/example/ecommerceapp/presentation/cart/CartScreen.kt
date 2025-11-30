package com.example.ecommerceapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.models.CartItem
import com.example.ecommerceapp.data.viewmodels.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    var selectedShipping by remember { mutableStateOf("Free shipping") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1B3C))
            .padding(16.dp)
            .padding(innerPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("My Cart", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cartItems, key = { it.product.id }) { cartItem ->
                CartItemComposable(
                    cartItem = cartItem,
                    onRemove = {
                        scope.launch {
                            cartViewModel.removeFromCart(cartItem.product.id)
                        }
                    },
                    onQuantityChange = { newQty ->
                        scope.launch {
                            cartViewModel.updateProductQuantity(cartItem.product, newQty)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        val subtotal = cartItems.sumOf { it.product.price * it.quantity }

        val shippingCost = when (selectedShipping) {
            "Flat rate" -> 20.0
            "Local pickup" -> 25.0
            else -> 0.0
        }
        val total = subtotal + shippingCost

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2750), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                "Subtotal: $${"%.2f".format(subtotal)}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Shipping Method", color = Color.LightGray)

            Column {
                ShippingOption("Flat rate", 20.0, selectedShipping) { selectedShipping = it }
                ShippingOption("Local pickup", 25.0, selectedShipping) { selectedShipping = it }
                ShippingOption("Free shipping", 0.0, selectedShipping) { selectedShipping = it }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total: $${"%.2f".format(total)}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate("checkout/${total.toString()}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Checkout Now")
            }
        }
    }
}


@Composable
fun CartItemComposable(
    cartItem: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    var quantity by remember(cartItem.quantity) { mutableStateOf(cartItem.quantity) }

    LaunchedEffect(cartItem.quantity) {
        quantity = cartItem.quantity
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2750), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Red)
        }

        AsyncImage(
            model = cartItem.product.thumbnail,
            contentDescription = cartItem.product.title,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(cartItem.product.title, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                "$${"%.2f".format(cartItem.product.price * quantity)}",
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (quantity > 1) {
                        onQuantityChange(quantity - 1)
                    }
                },
                enabled = quantity > 1
            ) {
                Text("-", color = if (quantity > 1) Color.White else Color.Gray, fontSize = 18.sp)
            }

            Text(
                quantity.toString(),
                color = Color.White,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    onQuantityChange(quantity + 1)
                }
            ) {
                Text("+", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ShippingOption(name: String, price: Double, selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(name) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == name,
            onClick = { onSelect(name) },
            colors = RadioButtonDefaults.colors(selectedColor = Color.Green)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("$name: $${"%.2f".format(price)}", color = Color.White)
    }
}