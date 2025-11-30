package com.example.ecommerceapp.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.ecommerceapp.data.models.Product
import com.google.firebase.auth.FirebaseAuth

class FavoritesRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val favoritesCollection =
        firestore.collection("users").document(auth.uid.toString()).collection("favorites")

    suspend fun addFavorite(product: Product) {

        val productMap = mapOf(
            "id" to product.id,
            "title" to product.title,
            "price" to product.price,
            "thumbnail" to product.thumbnail
        )

        favoritesCollection.document(product.id.toString())
            .set(productMap)
            .await()
    }

    suspend fun removeFavorite(productId: Int) {
        favoritesCollection.document(productId.toString())
            .delete()
            .await()
    }

    suspend fun fetchFavoriteIds(): Set<Int> {
        return try {
            val snapshot = favoritesCollection.get().await()
            Log.d(
                "FavoritesRepo", "Favorites fetched: ${
                snapshot.documents
                    .mapNotNull { it.getLong("id")?.toInt() }
                    .toSet()
            }")
            snapshot.documents
                .mapNotNull { it.getLong("id")?.toInt() }
                .toSet()

        } catch (e: Exception) {
            Log.e("FavoritesRepo", "Error: ${e.message}")
            emptySet()
        }
    }
}
