package com.example.ecommerceapp.data.models

data class WishList(
    val userId: String="",
    val products: List<Int> = emptyList()
)