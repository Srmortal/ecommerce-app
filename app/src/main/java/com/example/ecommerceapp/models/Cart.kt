package com.example.ecommerceapp.models

data class Cart(
    val id: Int,
    val userId: String,
    val products: List<String>,
    val totalPrice: Double,
    val fireId: String,
)
