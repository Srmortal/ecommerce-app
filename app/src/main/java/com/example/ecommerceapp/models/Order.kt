package com.example.ecommerceapp.models

data class Order(
    val id: Int,
    val userId: String,
    val products: List<String>,
    val totalPrice: Double,
    val status: String,
    val date: String,
    val fireId: String,
)
