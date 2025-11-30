package com.example.ecommerceapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.CartItem
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.repo.CartRepository // Assuming CartRepository exists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()


    fun fetchCartItems() {
        viewModelScope.launch {
            try {
                _cartItems.value = cartRepository.fetchCartItems()
                Log.d("CartViewModel", "Fetched ${_cartItems.value.size} cart items.")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error fetching cart items: ${e.message}")
            }
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            try {
                cartRepository.removeFromCart(productId)

                _cartItems.value = _cartItems.value.filter { it.product.id != productId }
                Log.d("CartViewModel", "Removed product $productId from cart.")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error removing product $productId: ${e.message}")
            }
        }
    }

    fun updateProductQuantity(product: Product, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(product.id)
            return
        }

        viewModelScope.launch {
            try {
                cartRepository.updateProductQuantity(product, newQuantity)

                _cartItems.value = _cartItems.value.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = newQuantity)
                    } else {
                        item
                    }
                }
                Log.d("CartViewModel", "Updated quantity for ${product.id} to $newQuantity.")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error updating quantity for ${product.id}: ${e.message}")
            }
        }
    }

    fun addProductToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                cartRepository.addToCart(product, quantity)

                fetchCartItems()

                Log.d("CartViewModel", "Added product ${product.id} with quantity $quantity.")
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error adding product: ${e.message}")
            }
        }
    }

}