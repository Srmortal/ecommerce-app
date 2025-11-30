package com.example.ecommerceapp.data.repo

import android.util.Log
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.models.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class CartRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    suspend fun addToCart(product: Product, quantityToAdd: Int) {
        val userId = currentUserId ?: run {
            Log.e("CartRepository", "No logged-in user")
            return
        }

        val cartRef = firestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(product.id.toString())

        try {
            val existingDoc = cartRef.get().await()

            if (existingDoc.exists()) {
                cartRef.update("quantity", FieldValue.increment(quantityToAdd.toLong())).await()
                Log.d("CartRepository", "Incremented quantity for product: ${product.title} by $quantityToAdd.")

            } else {
                val cartItemData = mapOf(
                    "id" to product.id,
                    "title" to product.title,
                    "description" to product.description,
                    "price" to product.price,
                    "category" to product.category,
                    "images" to product.images,
                    "brand" to product.brand,
                    "rating" to product.rating,
                    "stock" to product.stock,
                    "thumbnail" to product.thumbnail,
                    "discountPercentage" to product.discountPercentage,
                    "fireId" to product.fireId,
                    "quantity" to quantityToAdd
                )

                cartRef.set(cartItemData).await()
                Log.d("CartRepository", "Added new product to cart: ${product.title} with quantity: $quantityToAdd")
            }

        } catch (e: Exception) {
            Log.e("CartRepository", "Error adding/updating product to cart", e)
        }
    }

    suspend fun fetchCartItems(): List<CartItem> {
        val userId = currentUserId ?: return emptyList()

        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("cart")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                val quantity = document.getLong("quantity")?.toInt() ?: 1
                val product = document.toObject(Product::class.java)?.apply {
                    fireId = document.id
                }

                if (product != null) {
                    CartItem(product = product, quantity = quantity)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("CartRepository", "Error fetching cart items", e)
            emptyList()
        }
    }

    suspend fun removeFromCart(productId: Int) {
        val userId = currentUserId ?: run {
            Log.e("CartRepository", "No logged-in user to remove item")
            return
        }

        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(productId.toString())
            .delete()
            .await()
        Log.d("CartRepository", "Removed product $productId from cart.")
    }

    suspend fun updateProductQuantity(product: Product, newQuantity: Int) {
        val userId = currentUserId ?: run {
            Log.e("CartRepository", "No logged-in user to update quantity")
            return
        }

        val updates = mapOf(
            "quantity" to newQuantity
        )

        firestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(product.id.toString())
            .update(updates)
            .await()
        Log.d("CartRepository", "Updated quantity for ${product.id} to $newQuantity.")
    }
}