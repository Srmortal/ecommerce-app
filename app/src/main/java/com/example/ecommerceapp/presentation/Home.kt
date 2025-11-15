package com.example.ecommerceapp.presentation

import AppBar
import BottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.models.Category
import com.example.ecommerceapp.data.viewmodels.CategoryViewModel
import com.example.ecommerceapp.data.viewmodels.ProductViewModel

@Composable
fun Home(modifier: Modifier = Modifier){
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar()
        },
        bottomBar = {
            BottomBar(
                navController = rememberNavController(),
                currentRoute = "home"
            )
        }
    ) { innerPadding ->
        HomeScreen(modifier = Modifier.padding(innerPadding))
    }
}

// in: presentation/Home.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        categoryViewModel.fetchCategories()
    }
    val categories by categoryViewModel.categories.observeAsState(emptyList())
    val searchResults by productViewModel.searchResults.observeAsState(emptyList())
    var searchText by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }


    Column(
        // Use the modifier passed into the composable
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Adjusted padding
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                        // --- FIX 1: Search as the user types ---
                        // To avoid too many queries, only search if the text is long enough.
                        if (it.length > 2) {
                            productViewModel.searchProducts(it)
                        }
                    },
                    onSearch = { queryText ->
                        // --- FIX 2: Search when the user presses 'Enter'/'Search' ---
                        productViewModel.searchProducts(queryText)
                        expanded = false // Hide the keyboard and search results view
                    },
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = {
                        Text(text = "Search")
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = {
                expanded = it
                // Clear results if the user closes the search suggestions
                // without performing a search.
                if (!it) {
                    productViewModel.searchProducts("") // Clears the list
                }
            },
            modifier = Modifier.padding(PaddingValues(vertical = 2.dp))
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // This part will now correctly show results as they are fetched.
                searchResults.forEach { product ->
                    ListItem(
                        headlineContent = {
                            Text(text = product.title)
                        },
                        supportingContent = {
                            Text(text = product.description, maxLines = 2)
                        },
                        leadingContent = {
                            AsyncImage(
                                // Use the thumbnail for better performance in a list
                                model = product.images[0],
                                contentDescription = product.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape) // Makes the image round
                            )
                        },
                        modifier = Modifier.clickable {
                            // TODO: Handle navigation to product detail
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Add a title for the categories section
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories.size) {
                CategoryItem(category = categories[it])
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp),
    ){
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 60.dp, height = 40.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = category.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview(){
    Home()
}