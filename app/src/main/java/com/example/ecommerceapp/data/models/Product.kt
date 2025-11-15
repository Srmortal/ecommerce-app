package com.example.ecommerceapp.data.models

data class Product(
    val id: Int=0,
    val title: String="",
    val description: String="",
    val price: Double=0.0,
    val category: String="",
    val images: List<String> = emptyList(),
    val brand: String="",
    val rating: Double=0.0,
    val stock: Int=0,
    val thumbnail: String="",
    val discountPercentage: Double=0.0,
    var fireId: String=""
)
data class ProductList(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)