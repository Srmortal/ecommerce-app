package com.example.ecommerceapp.data.models

data class Cart(
    val id: Int,
    val userId: Int,
    val totalPrice: Double,
    val totalProducts: Int,
    val totalQuantity: Int,
    val fireId: String
)
