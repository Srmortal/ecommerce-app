package com.example.ecommerceapp.data.models

import java.util.Date

data class Review(
    val id: Int,
    val userId: Int,
    val productId: Int,
    val rating: Double,
    val comment: String,
    val reviewDate: Date,
    val fireId: String
)
