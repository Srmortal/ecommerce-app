package com.example.ecommerceapp.presentation.whistlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.Constants
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.presentation.viewmodels.CartViewModel
import com.example.ecommerceapp.presentation.viewmodels.ProductViewModel
import com.example.ecommerceapp.ui.theme.AppBar
import com.example.ecommerceapp.ui.theme.RoyalBlue

@Composable
fun WishlistScreen(
    innerPaddingValues: PaddingValues,
    navController: NavController,
    onShowSnackbar: (String) -> Unit,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val allProducts by productViewModel.products.collectAsState()
    val favoriteIds by productViewModel.favoriteIds.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()

    val wishlistProducts = remember(allProducts, favoriteIds) {
        allProducts.filter { product -> favoriteIds.contains(product.id) }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        productViewModel.fetchFavorites()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppBar
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPaddingValues)
                        .padding(Constants.screenPadding),
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

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C153B))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wishlist Items (${wishlistProducts.size})",
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
                    items(wishlistProducts, key = { it.id }) { product ->
                        WishlistItemCard(
                            product = product,
                            onRemoveClick = {
                                productViewModel.toggleFavorite(product)
                            }
                        )
                    }
                }
            }

            if (wishlistProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        wishlistProducts.forEach { product ->
                            cartViewModel.addProductToCart(product, 1)
                        }
                        onShowSnackbar("All items added to cart!")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 60.dp)
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
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistItemCard(
    product: Product,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        // Brighter background to provide contrast against the deep blue screen background
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Lighter card surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Fixed height for consistency
        ) {
            // Product Image Container
            Box(
                modifier = Modifier
                    .width(110.dp) // Wider image area
                    .fillMaxHeight()
                    .background(Color(0xFFF0F0F0)), // Light gray background for image area
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.images.firstOrNull() ?: product.thumbnail,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp) // Padding for image inside the Box
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and Remove Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black.copy(alpha = 0.8f), // Darker text on white background
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Remove Button (Trash Icon)
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(32.dp) // Slightly larger touch target
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove from wishlist",
                            tint = Color.Red.copy(alpha = 0.7f) // Subtler red tint
                        )
                    }
                }

                // Price and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${String.format("%.1f", product.rating)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }

                    // Price (Primary focus)
                    val originalPrice = if (product.discountPercentage > 0) {
                        product.price / (1 - product.discountPercentage / 100)
                    } else {
                        product.price
                    }

                    val priceText = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = RoyalBlue, // Use Royal Blue for emphasis
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize // Make price larger
                            )
                        ) {
                            append("$${String.format("%.2f", product.price)}")
                        }
                        if (product.discountPercentage > 0) {
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            ) {
                                append("$${String.format("%.2f", originalPrice)}")
                            }
                        }
                    }
                    Text(text = priceText)
                }
            }
        }
    }
}