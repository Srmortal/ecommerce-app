package com.example.ecommerceapp.presentation.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
    onResultClick: (Product) -> Unit,
    onFilterChange: (FilterState) -> Unit = {},
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showBrandDialog by remember { mutableStateOf(false) }

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box {
                    IconButton(
                        onClick = {
                            menuExpanded = true
                        }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color(0xFF747794)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by Price: Low to High") },
                            onClick = {
                                onFilterChange(FilterState(sortOperation = { list -> list.sortedBy { it.price } }))
                                menuExpanded = false // Close the menu after clicking
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Price: High to Low") },
                            onClick = {
                                onFilterChange(
                                    FilterState(
                                        sortOperation = { list -> list.sortedByDescending { it.price } }
                                    )
                                )
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Filter by Brand...") },
                            onClick = {
                                showBrandDialog = true
                                menuExpanded = false
                            }
                        )
                    }
                }
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { onSearchTextChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color(0xFF747794)
                        )
                    }
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
    if (showBrandDialog) {
        BrandFilteringDialog(
            allBrands = results.map { it.brand }.filter { it.isNotBlank() }.toSet().toList(),
            selectedBrands = emptySet(),
            onDismiss = { showBrandDialog = false },
            onApply = {
                onFilterChange(FilterState(selectedBrands = it))
                showBrandDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandFilteringDialog(
    allBrands: List<String>,
    selectedBrands: Set<String>,
    onDismiss: () -> Unit,
    onApply: (Set<String>) -> Unit
) {
    Log.d("BrandFilteringDialog", "selectedBrands: $selectedBrands")
    Log.d("BrandFilteringDialog", "allBrands: $allBrands")
    var tmpSelectedBrands by remember { mutableStateOf(selectedBrands) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter by Brand") },
        text = {
            LazyColumn {
                items(allBrands) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                                tmpSelectedBrands = if (tmpSelectedBrands.contains(it)) {
                                    tmpSelectedBrands - it
                                } else {
                                    tmpSelectedBrands + it
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = tmpSelectedBrands.contains(it),
                            onCheckedChange = null
                        )
                        Text(it, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onApply(tmpSelectedBrands) }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}