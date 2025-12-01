package com.example.ecommerceapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.OrderStatus
import com.example.ecommerceapp.data.models.PaymentMethod
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class OrderViewModel : ViewModel() {
    private val _fireId = MutableStateFlow<String?>(null)
    val fireId: StateFlow<String?> = _fireId

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _deliveryAddress = MutableStateFlow<String?>(null)
    val deliveryAddress: StateFlow<String?> = _deliveryAddress.asStateFlow()
    private val _isOrdersLoading = MutableStateFlow(false)
    val isOrdersLoading: StateFlow<Boolean> = _isOrdersLoading.asStateFlow()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    init {
        auth.currentUser?.uid?.let {
            _userId.value = it
        }
    }

    fun setOrderDetails(deliveryAddress: String) {
        _deliveryAddress.value = deliveryAddress
    }

    fun confirmOrder(
        userId: String?,
        paymentMethod: PaymentMethod,
        deliveryAddress: String?,
        onFailure: (Exception) -> Unit
    ) {
        Log.d("OrderViewModel", "confirmOrder called with userId: $userId")
        Log.d("OrderViewModel", "confirmOrder called with paymentMethod: $paymentMethod")
        Log.d("OrderViewModel", "confirmOrder called with fireId: $fireId")
        Log.d("OrderViewModel", "confirmOrder called with deliveryAddress: $deliveryAddress")

        val uid = auth.currentUser?.uid
        if (uid == null) {
            onFailure(IllegalStateException("User not authenticated."))
            return
        }

        if (userId == null || deliveryAddress.isNullOrBlank()) {
            onFailure(IllegalStateException("Missing required order details (userId or deliveryAddress)."))
            return
        }

        val newOrder = Order(
            fireId = null,
            paymentMethod = paymentMethod,
            orderDate = Date(),
            status = OrderStatus.PENDING,
            deliveryAddress = deliveryAddress,
        )

        viewModelScope.launch {
            try {
                usersCollection
                    .document(uid)
                    .collection("orders")
                    .add(newOrder)
                    .addOnSuccessListener {
                        it.update("fireId", it.id)
                    }
                    .await()


            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun fetchOrders() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _orders.value = emptyList()
            return
        }

        _isOrdersLoading.value = true
        viewModelScope.launch {
            try {
                val ordersCollection = usersCollection
                    .document(uid)
                    .collection("orders")
                    .orderBy("orderDate", Query.Direction.DESCENDING)

                val snapshot = ordersCollection.get().await()

                val fetchedOrders = snapshot.documents.mapNotNull { document ->
                    document.toObject(Order::class.java)?.copy(fireId = document.id)
                }

                _orders.value = fetchedOrders

            } catch (e: Exception) {
                _orders.value = emptyList()
            } finally {
                _isOrdersLoading.value = false
            }
        }
    }
}