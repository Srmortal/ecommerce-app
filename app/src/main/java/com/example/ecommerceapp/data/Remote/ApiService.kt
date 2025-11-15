package com.example.ecommerceapp.data.Remote

import com.example.ecommerceapp.data.models.Category
import com.example.ecommerceapp.data.models.ProductList
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): ProductList

    @GET("products/categories")
    suspend fun getCategories(): List<Category>
}