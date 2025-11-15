package com.example.ecommerceapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.models.WishList
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WishlistViewModel : ViewModel() {
    private val _wishlistProducts = MutableLiveData<List<Product>>()
    val wishlistProducts: LiveData<List<Product>> = _wishlistProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWishlist() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _error.postValue(null)
            
            try {
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                
                if (currentUser == null) {
                    _error.postValue("User not authenticated")
                    _wishlistProducts.postValue(emptyList())
                    _isLoading.postValue(false)
                    return@launch
                }

                val firestore = Firebase.firestore
                
                // Fetch user's wishlist
                val wishlistQuery = firestore.collection("wishlists")
                    .whereEqualTo("userId", currentUser.uid)
                    .limit(1)
                    .get()
                    .await()

                if (wishlistQuery.isEmpty) {
                    // Create empty wishlist if it doesn't exist
                    _wishlistProducts.postValue(emptyList())
                    _isLoading.postValue(false)
                    return@launch
                }

                val wishlistDoc = wishlistQuery.documents.first()
                val wishlist = wishlistDoc.toObject(WishList::class.java)
                
                if (wishlist == null || wishlist.products.isEmpty()) {
                    _wishlistProducts.postValue(emptyList())
                    _isLoading.postValue(false)
                    return@launch
                }

                // Fetch product details for each product ID in wishlist
                val products = mutableListOf<Product>()
                
                for (productId in wishlist.products) {
                    try {
                        // Try to find product by id field (from API) or fireId
                        val productQuery = firestore.collection("products")
                            .whereEqualTo("id", productId)
                            .limit(1)
                            .get()
                            .await()
                        
                        if (!productQuery.isEmpty) {
                            val product = productQuery.documents.first().toObject(Product::class.java)
                            product?.let { products.add(it) }
                        }
                    } catch (e: Exception) {
                        Log.e("WishlistViewModel", "Error fetching product $productId", e)
                    }
                }

                _wishlistProducts.postValue(products)
                Log.d("WishlistViewModel", "Fetched ${products.size} wishlist products")
                
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error fetching wishlist", e)
                _error.postValue(e.message ?: "Failed to fetch wishlist")
                _wishlistProducts.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun removeFromWishlist(productId: Int) {
        viewModelScope.launch {
            try {
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                
                if (currentUser == null) {
                    _error.postValue("User not authenticated")
                    return@launch
                }

                val firestore = Firebase.firestore
                
                // Find user's wishlist
                val wishlistQuery = firestore.collection("wishlists")
                    .whereEqualTo("userId", currentUser.uid)
                    .limit(1)
                    .get()
                    .await()

                if (wishlistQuery.isEmpty) {
                    return@launch
                }

                val wishlistDoc = wishlistQuery.documents.first()
                val wishlist = wishlistDoc.toObject(WishList::class.java)
                
                if (wishlist != null) {
                    // Remove product ID from list
                    val updatedProducts = wishlist.products.filter { it != productId }
                    
                    // Update wishlist in Firebase
                    firestore.collection("wishlists")
                        .document(wishlistDoc.id)
                        .update("products", updatedProducts)
                        .await()
                    
                    // Refresh wishlist
                    fetchWishlist()
                }
                
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error removing from wishlist", e)
                _error.postValue(e.message ?: "Failed to remove item")
            }
        }
    }
}

