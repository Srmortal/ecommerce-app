package com.example.ecommerceapp.data.models

data class CartItem(
    val id: Int,
    val productId: Int,
    val cartId: Int,
    val quantity: Int,
    val price: Double,
    val fireId: String
)
