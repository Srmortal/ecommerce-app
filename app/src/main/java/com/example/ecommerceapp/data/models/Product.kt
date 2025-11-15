package com.example.ecommerceapp.data.models

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val categoryId: Int,
    val images: List<String>,
    val brand: String,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val fireId: String,
    val name: String,
    val image: Int
)

