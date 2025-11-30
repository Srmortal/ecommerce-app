package com.example.ecommerceapp.data.models

data class CartItem(
    val product: Product = Product(),
    val quantity: Int = 1
)