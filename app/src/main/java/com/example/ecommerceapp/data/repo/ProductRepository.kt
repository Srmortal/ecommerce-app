package com.example.ecommerceapp.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository  {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun toggleFavorite(productId: Int): Boolean {
        val userId = currentUserId ?: throw Exception("User not authenticated.")

        val favoriteRef = firestore.collection("users").document(userId)
            .collection("favorites").document(productId.toString())

        val snapshot = favoriteRef.get().await()

        return if (snapshot.exists()) {
            favoriteRef.delete().await()
            false
        } else {
            val data = mapOf(
                "productId" to productId,
                "timestamp" to FieldValue.serverTimestamp()
            )
            favoriteRef.set(data).await()
            true
        }
    }


}