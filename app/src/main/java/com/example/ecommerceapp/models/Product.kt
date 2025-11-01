package com.example.ecommerceapp.models

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: Int,
    val thumbnail: String,
    val images: List<String>,
    val fireId: String,
)
