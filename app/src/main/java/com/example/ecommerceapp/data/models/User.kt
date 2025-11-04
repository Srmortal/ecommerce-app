package com.example.ecommerceapp.data.models

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val address: String,
    val image: String,
    val fireId: String
)
