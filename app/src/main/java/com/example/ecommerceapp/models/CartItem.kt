package com.example.ecommerceapp.models

data class CartItem(
    val id: Int,
    val cartId: Int,
    val productId: Int,
    val quantity: Int,
    val total: Double,
    val fireId: String,
)
