package com.example.ecommerceapp.presentation.home

import com.example.ecommerceapp.data.models.Product

data class FilterState(
    val sortOperation: (List<Product>) -> List<Product> = { it }, // The sorting delegate
    val selectedBrands: Set<String> = emptySet() // The set of selected brands
)
