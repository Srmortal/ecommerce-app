package com.example.ecommerceapp.data.models

data class Category(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val subCategoryId: String?,
    val fireId: String,
)