package com.example.ecommerceapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.viewmodels.WishlistViewModel
import com.example.ecommerceapp.ui.theme.AppBar
import com.example.ecommerceapp.ui.theme.BlueLogo1
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.RoyalBlue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun WishlistScreen(
    navController: NavController,
    viewModel: WishlistViewModel = viewModel()
) {
    val wishlistProducts by viewModel.wishlistProducts.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    LaunchedEffect(Unit) {
        viewModel.fetchWishlist()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueLogo1)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AppBar
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                
                Text(
                    text = "Wishlist",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wishlist Items",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "Error loading wishlist",
                        color = Color.White
                    )
                }
            } else if (wishlistProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your wishlist is empty",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(wishlistProducts) { product ->
                        WishlistItemCard(
                            product = product,
                            onDelete = {
                                viewModel.removeFromWishlist(product.id)
                            }
                        )
                    }
                }
            }

            // Add all items to cart button
            if (wishlistProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        addAllToCart(wishlistProducts)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoyalBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Add to cart",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add all items to cart",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun addAllToCart(products: List<Product>) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    
    if (currentUser == null) return
    
    val firestore = Firebase.firestore
    
    // Get or create user's cart
    firestore.collection("carts")
        .whereEqualTo("userId", currentUser.uid)
        .limit(1)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // Create new cart
                val cartData = hashMapOf(
                    "userId" to currentUser.uid,
                    "items" to products.map { product ->
                        hashMapOf(
                            "productId" to product.id,
                            "quantity" to 1,
                            "price" to product.price
                        )
                    }
                )
                firestore.collection("carts").add(cartData)
            } else {
                // Update existing cart
                val cartDoc = querySnapshot.documents.first()
                val existingItems = cartDoc.get("items") as? List<Map<String, Any>> ?: emptyList()
                val newItems = products.map { product ->
                    hashMapOf(
                        "productId" to product.id,
                        "quantity" to 1,
                        "price" to product.price
                    )
                }
                val allItems = existingItems + newItems
                cartDoc.reference.update("items", allItems)
            }
        }
        .addOnFailureListener { e ->
            // Handle error
            e.printStackTrace()
        }
}

@Composable
fun WishlistItemCard(
    product: Product,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Product Image with notification dot
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(116.dp)
            ) {
                AsyncImage(
                    model = if (product.images.isNotEmpty()) {
                        product.images[0]
                    } else {
                        product.thumbnail
                    },
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
                
                // Notification dot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(12.dp)
                        .background(
                            color = Color.Red,
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price with strikethrough
                val originalPrice = if (product.discountPercentage > 0) {
                    product.price / (1 - product.discountPercentage / 100)
                } else {
                    product.price
                }
                
                val priceText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("$${String.format("%.2f", product.price)}")
                    }
                    if (product.discountPercentage > 0) {
                        append("  ")
                        withStyle(
                            style = SpanStyle(
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        ) {
                            append("$${String.format("%.2f", originalPrice)}")
                        }
                    }
                }
                Text(text = priceText)

                Spacer(modifier = Modifier.height(8.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Calculate review count from rating (approximation)
                    val reviewCount = (product.rating * 10).toInt().coerceAtLeast(1)
                    Text(
                        text = "${String.format("%.2f", product.rating)} ($reviewCount reviews)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishlistScreenPreview() {
    EcommerceAppTheme(darkTheme = false) {
        WishlistScreen(
            navController = rememberNavController()
        )
    }
}
