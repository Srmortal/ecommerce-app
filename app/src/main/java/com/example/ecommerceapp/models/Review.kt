package com.example.ecommerceapp.models

data class Review(
    val id: Int,
    val productId: Int,
    val userId: String,
    val rating: Double,
    val comment: String,
    val date: String,
    val fireId: String,
)
