package com.example.ecommerceapp.models

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val subCategoryId: String,
    val fireId: String,
)
