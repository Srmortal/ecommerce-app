package com.example.ecommerceapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.Order
import com.example.ecommerceapp.data.models.OrderStatus
import com.example.ecommerceapp.data.models.PaymentMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class OrderViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _userId = MutableStateFlow(auth.currentUser?.uid ?: "")
    val userId = _userId.asStateFlow()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isOrdersLoading = MutableStateFlow(false)
    val isOrdersLoading = _isOrdersLoading.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun fetchOrders() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e("OrderViewModel", "User not logged in")
            return
        }
        viewModelScope.launch {

            _isOrdersLoading.value = true
            firestore.collection("users")
                .document(uid)
                .collection("orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val orderList = result.map { document ->
                        val order = document.toObject(Order::class.java)
                        order.fireId = document.id
                        order
                    }
                    _orders.value = orderList
                    _isOrdersLoading.value = false
                }
                .addOnFailureListener { e ->
                    Log.e("OrderViewModel", "Error fetching orders", e)
                    _isOrdersLoading.value = false
                }
        }
    }

    fun confirmOrder(
        userId: String,
        paymentMethod: PaymentMethod,
        deliveryAddress: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderCollection = firestore.collection("users")
                    .document(userId)
                    .collection("orders")

                val newDocRef = orderCollection.document()

                val newOrder = Order(
                    fireId = newDocRef.id,
                    userId = userId,
                    paymentMethod = paymentMethod,
                    orderDate = Date(),
                    status = OrderStatus.PENDING,
                    deliveryAddress = deliveryAddress
                )
                newDocRef.set(newOrder)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}