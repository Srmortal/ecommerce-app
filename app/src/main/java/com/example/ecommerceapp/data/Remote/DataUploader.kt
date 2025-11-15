package com.example.ecommerceapp.data.Remote
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object DataUploader {

    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun fetchAndUploadProducts() {
        val firestore = Firebase.firestore
        // Check if data already exists to avoid re-uploading
        val existingDocs = firestore.collection("products").limit(1).get().await()
        if (!existingDocs.isEmpty) {
            Log.d("DataUploader", "Products collection is not empty. Skipping upload.")
            return
        }

        try {
            Log.d("DataUploader", "Fetching products from API...")
            val productList = api.getProducts()
            Log.d("DataUploader", "Fetched ${productList.products.size} products. Uploading to Firestore...")

            productList.products.forEach { product ->
                firestore.collection("products").add(product)
                    .addOnSuccessListener {
                        product.fireId = it.id
                        firestore.collection("products").document(it.id).set(product)
                        Log.d("DataUploader", "Product ${product.title} added successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("DataUploader", "Error adding product ${product.title}", e)
                    }
            }
        } catch (e: Exception) {
            Log.e("DataUploader", "Error fetching or uploading data", e)
        }
    }

    suspend fun fetchAndUploadCategories() {
        val firestore = Firebase.firestore
        val existingDocs = firestore.collection("categories").limit(1).get().await()
        if (!existingDocs.isEmpty()) {
            Log.d("DataUploader", "Categories collection is not empty. Skipping upload.")
            return
        }

        try {
            Log.d("DataUploader", "Fetching categories from API...")
            val categoryList = api.getCategories()
            Log.d("DataUploader", "Fetched ${categoryList.size} categories. Uploading to Firestore...")

            categoryList.forEach { category ->
                firestore.collection("categories").add(category)
                    .addOnSuccessListener {
                        category.Id = it.id
                        firestore.collection("categories").document(it.id).set(category)
                        Log.d("DataUploader", "Category ${category.name} added successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("DataUploader", "Error adding category ${category.name}", e)
                    }
            }
        } catch (e: Exception) {
            Log.e("DataUploader", "Error fetching or uploading data", e)
        }
    }
}
