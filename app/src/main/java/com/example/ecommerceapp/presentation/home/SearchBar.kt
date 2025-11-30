package com.example.ecommerceapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.presentation.AccentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    results: List<Product>,
    onResultClick: (Product) -> Unit
) {
    SearchBar(
        query = searchText,
        onQueryChange = onSearchTextChange,
        onSearch = onSearch,
        active = expanded,
        onActiveChange = onExpandedChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color(0xFF242644),
            dividerColor = Color.Transparent,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = AccentColor
            )
        ),
        placeholder = { Text("Search products...", color = Color(0xFF747794)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF747794)
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color(0xFF747794)
                    )
                }
            }
        },
        content = {
            // No changes needed here, as the problem was in the container structure.
            // When active, the SearchBar should now correctly use screen constraints.
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (results.isEmpty() && searchText.isNotEmpty()) {
                    item {
                        Text(
                            "No results found.",
                            modifier = Modifier.padding(8.dp),
                            color = Color.White
                        )
                    }
                } else {
                    items(results) { product ->
                        CompactProductItem(product = product, onClick = { onResultClick(product) })
                    }
                }
            }
        }
    )
}
