package com.example.ecommerceapp.data.Remote

import com.example.ecommerceapp.data.models.Category
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.models.ProductList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int=10,
        @Query("skip") skip: Int=5
    ): ProductList

    @GET("products/category-list")
    suspend fun getCategoryList(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int=10,
        @Query("skip") skip: Int=5
    ): ProductList
    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product
}