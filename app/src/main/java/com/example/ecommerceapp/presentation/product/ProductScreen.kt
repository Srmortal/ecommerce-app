package com.example.ecommerceapp.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.models.Review
import com.example.ecommerceapp.data.viewmodels.ProductViewModel
import com.example.ecommerceapp.data.viewmodels.CartViewModel
import com.example.ecommerceapp.data.viewmodels.SingleProductViewModel
import kotlinx.coroutines.launch

val DarkBackground = Color(0xFF0C153B)
val CardBackground = Color(0xFF242644)
val AccentColor = Color(0XFFFFAF00)
val PriceRed = Color(0xFFFF4C5E)
val ButtonPurple = Color(0xFF5E59FF)
val TextGray = Color(0xFF8F9BB3)

@Composable
fun SingleProductScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    singleProductViewModel: SingleProductViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val product by singleProductViewModel.product.collectAsState()
    val isLoading by singleProductViewModel.isLoading.collectAsState()
    val relatedProducts by productViewModel.relatedProducts.collectAsState()

    var quantity by remember { mutableIntStateOf(1) }

    val favoriteIds by productViewModel.favoriteIds.collectAsState(initial = emptySet())
    Log.d("FavoriteIds", favoriteIds.toString())
    val isFavorite = favoriteIds.contains(productId)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showSnackbar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(product) {
        productViewModel.fetchFavorites()

        val currentProduct = product
        if (currentProduct != null) {
            productViewModel.fetchRelatedProducts(
                category = currentProduct.category,
                currentProductId = currentProduct.id
            )
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            CustomTopBar(title = product?.title ?: "Loading...", onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentColor)
                }
            }
            product == null -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Product not found or failed to load.", color = Color.White)
                }
            }
            else -> {
                val currentProduct = product!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(bottom = 30.dp)
                ) {
                    item {
                        ProductHeaderSection(
                            product = currentProduct,
                            isFavorite = isFavorite,
                            onFavoriteToggle = { productViewModel.toggleFavorite(currentProduct) }
                        )
                    }

                    item {
                        QuantityAndCartSection(
                            quantity = quantity,
                            onQuantityChange = { quantity = it },
                            onAddToCart = { qty ->
                                cartViewModel.addProductToCart(currentProduct, qty)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "${qty}x ${currentProduct.title} added to cart!",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }

                    item { SpecificationsSection(currentProduct.description) }

                    item {
                        RelatedProductsSection(
                            relatedProducts = relatedProducts,
                            onProductClick = onProductClick,
                            cartViewModel = cartViewModel,
                            onProductAddedToCart = { product ->
                                showSnackbar("${product.title} added to cart!")
                            }
                        )
                    }

                    item { ReviewsSection(currentProduct.reviews) }

                }
            }
        }
    }
}


@Composable
fun ProductHeaderSection(
    product: Product,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Color.White.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.images.firstOrNull() ?: "",
                contentDescription = product.title,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play",
                tint = DarkBackground,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = PriceRed,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val discountedPrice = product.price * (1 - product.discountPercentage / 100)
            Text(
                text = "$${String.format("%.2f", discountedPrice)}",
                style = MaterialTheme.typography.headlineSmall,
                color = PriceRed,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textDecoration = TextDecoration.LineThrough
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(PriceRed, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${product.rating} Very Good",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            // Display stars based on product rating
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < product.rating.toInt()) AccentColor else TextGray.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("(${product.reviews.size} ratings)", color = TextGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QuantityAndCartSection(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(CardBackground, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Text("-", color = Color.White, fontSize = 20.sp)
            }
            Text(
                text = "$quantity",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { onQuantityChange(quantity + 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Text("+", color = Color.White, fontSize = 20.sp)
            }
        }

        Button(
            onClick = { onAddToCart(quantity) },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Text("Add To Cart", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SpecificationsSection(description: String) {
    Column {
        Text("Specifications", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description.ifBlank { "Lorem ipsum dolor sit amet consectetur adipisicing elit. Quasi, eum? Id, culpa? At officia quisquam laudantium nisi mollitia nesciunt." },
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@Composable
fun RelatedProductsSection(
    relatedProducts: List<Product>,
    onProductClick: (Int) -> Unit,
    cartViewModel: CartViewModel,
    onProductAddedToCart: (Product) -> Unit
) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Related Products", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(relatedProducts) { product ->
                CompactRelatedProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    cartViewModel = cartViewModel,
                    onAddToCart = { onProductAddedToCart(product) }
                )
            }
        }

        if (relatedProducts.isEmpty()) {
            Text("No related products found in this category.", color = TextGray, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun CompactRelatedProductCard(
    product: Product,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    onAddToCart: () -> Unit
) {
    val discountedPrice = product.price * (1 - product.discountPercentage / 100)

    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick)
            .background(CardBackground, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.height(100.dp).fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp))) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
            )
            Text(
                product.category,
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.padding(4.dp).background(Color(0xFF00C853), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            product.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$${String.format("%.0f", discountedPrice)}", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Text("$${product.price}", color = TextGray, fontSize = 10.sp, textDecoration = TextDecoration.LineThrough)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = String.format("%.1f", product.rating),
                color = AccentColor,
                fontSize = 10.sp
            )

            IconButton(
                onClick = {
                    cartViewModel.addProductToCart(product, 1)
                    onAddToCart()
                },
                modifier = Modifier
                    .background(ButtonPurple, CircleShape)
                    .size(20.dp)
                    .padding(2.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add to Cart",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column {
        Text("Ratings & Reviews (${reviews.size})", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (reviews.isEmpty()) {
            Text("No reviews yet.", color = TextGray, style = MaterialTheme.typography.bodyMedium)
        } else {
            reviews.forEach { review ->
                ReviewItem(
                    name = review.reviewerName,
                    date = review.date.substringBefore("T"),
                    comment = review.comment,
                    rating = review.rating
                )
            }
        }
    }
}

@Composable
fun ReviewItem(name: String, date: String, comment: String, rating: Int) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(name.first().toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            // Display stars based on the actual rating
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = AccentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(comment, color = TextGray, style = MaterialTheme.typography.bodyMedium)
            Text("$name, $date", color = TextGray.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = CardBackground, thickness = 1.dp)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, color = Color.White, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
    )
}