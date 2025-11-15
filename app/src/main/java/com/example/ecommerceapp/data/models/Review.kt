package com.example.ecommerceapp.data.models

import java.util.Date

data class Review(
    val id: Int=0,
    val userId: Int=0,
    val productId: Int=0,
    val rating: Double=0.0,
    val comment: String="",
    val reviewDate: Date=Date(),
    val fireId: String=""
)
