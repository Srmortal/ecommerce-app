package com.example.ecommerceapp.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.presentation.home.CategoryItem
import com.example.ecommerceapp.presentation.home.FilterState
import com.example.ecommerceapp.presentation.home.ProductGridItem
import com.example.ecommerceapp.presentation.home.SearchBar
import com.example.ecommerceapp.presentation.product.SkeletonProductItem
import com.example.ecommerceapp.presentation.viewmodels.CartViewModel
import com.example.ecommerceapp.presentation.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    onProductClick: (Int) -> Unit,
    onProductAddedToCart: (Product) -> Unit
) {
    var filterState by remember { mutableStateOf(FilterState()) }

    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val products by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val favoriteIds by productViewModel.favoriteIds.collectAsState()
    Log.d("FavoriteIds", favoriteIds.toString())
    val dynamicCategories by productViewModel.categories.collectAsState()

    val filteredProducts = remember(searchText, products, selectedCategory, filterState) {
        val filtered = products.filter { product ->
            val matchesSearch = if (searchText.isBlank()) true else {
                product.title.contains(searchText, ignoreCase = true)
            }
            val matchesCategory = if (selectedCategory == null) true else {
                product.category.contains(selectedCategory!!, ignoreCase = true) ||
                        selectedCategory!!.contains(product.category, ignoreCase = true)
            }
            matchesSearch && matchesCategory
        }
        val brandFiltered = if (filterState.selectedBrands.isEmpty()) {
            filtered
        } else {
            filtered.filter { it.brand in filterState.selectedBrands }
        }
        filterState.sortOperation(brandFiltered)
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
                    onSearch = { _ -> expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    results = filteredProducts,
                    onResultClick = { product: Product ->
                        onProductClick(product.id)
                    },
                    onFilterChange = {
                        filterState = filterState.copy(
                            sortOperation = it.sortOperation,
                            selectedBrands = if (it.selectedBrands.isNotEmpty()) it.selectedBrands else filterState.selectedBrands
                        )
                        if (it.selectedBrands.isEmpty() && it.sortOperation == {it}) {
                            filterState = filterState.copy(selectedBrands = emptySet())
                        }
                    },
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
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth(),
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