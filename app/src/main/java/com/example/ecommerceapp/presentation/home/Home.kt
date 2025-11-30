package com.example.ecommerceapp.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recommended for flows
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.viewmodels.CartViewModel
import com.example.ecommerceapp.data.viewmodels.ProductViewModel
import com.example.ecommerceapp.presentation.home.Category as UiCategory
import com.example.ecommerceapp.presentation.home.CategoryItem
import com.example.ecommerceapp.presentation.home.ProductGridItem
import com.example.ecommerceapp.presentation.home.SearchBar
import com.example.ecommerceapp.presentation.product.SkeletonProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    onProductClick: (Int) -> Unit,
    onProductAddedToCart: (Product) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val products by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val favoriteIds by productViewModel.favoriteIds.collectAsState()
    Log.d("FavoriteIds", favoriteIds.toString())
    val dynamicCategories by productViewModel.categories.collectAsState()

    val filteredProducts = remember(searchText, products, selectedCategory) {
        products.filter { product ->
            val matchesSearch = if (searchText.isBlank()) true else {
                product.title.contains(searchText, ignoreCase = true)
            }
            val matchesCategory = if (selectedCategory == null) true else {
                product.category.contains(selectedCategory!!, ignoreCase = true) ||
                        selectedCategory!!.contains(product.category, ignoreCase = true)
            }
            matchesSearch && matchesCategory
        }
    }

    val isSearching = searchText.isNotBlank()
    LaunchedEffect(selectedCategory) {
        productViewModel.fetchFavorites()
        productViewModel.loadProducts(selectedCategory)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0C153B)
                ),
                modifier = Modifier.height(70.dp)
            )
        },
        containerColor = Color(0xFF0C153B)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = {
                        searchText = it
                        expanded = true
                    },
                    onSearch = { query -> expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    results = filteredProducts,
                    onResultClick = { product: Product ->
                        onProductClick(product.id)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(dynamicCategories) { category ->
                        val isSelected = selectedCategory == category.title
                        CategoryItem(
                            category = category,
                            isSelected = isSelected,
                            onClick = {
                                selectedCategory = if (isSelected) null else category.title
                            }
                        )
                    }

                    // 2. Filter Status Text
                    item(span = { GridItemSpan(4) }) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            if (selectedCategory != null) {
                                Text(
                                    text = "Filtered by: $selectedCategory",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }

                    if (filteredProducts.isEmpty() && !isLoading) {
                        item(span = { GridItemSpan(4) }) {
                            Box(
                                modifier = Modifier.height(200.dp).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No products found.", color = Color.White)
                            }
                        }
                    } else {
                        items(
                            count = filteredProducts.size,
                            key = { index -> filteredProducts[index].id },
                            span = { GridItemSpan(2) }
                        ) { index ->
                            val product = filteredProducts[index]
                            val isFavorited = favoriteIds.contains(product.id)

                            if (index == filteredProducts.lastIndex && !isSearching && !isLoading) {
                                LaunchedEffect(Unit) {
                                    productViewModel.fetchProducts()
                                }
                            }

                            Box(modifier = Modifier.clickable { onProductClick(product.id) }) {
                                ProductGridItem(
                                    product = product,
                                    isFavorite = isFavorited,
                                    onFavoriteClick = { productViewModel.toggleFavorite(product) },
                                    onAddClick = {
                                        cartViewModel.addProductToCart(product, 1)
                                        onProductAddedToCart(product)
                                    }
                                )
                            }
                        }
                    }
                    if (isLoading) {
                        items(2, span = { GridItemSpan(2) }) {
                            SkeletonProductItem()
                        }
                    }
                }
            }
        }
    }
}