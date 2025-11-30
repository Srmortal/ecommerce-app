package com.example.ecommerceapp.data.viewmodels

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

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    private val _cartId = MutableStateFlow<Int?>(null)
    val cartId: StateFlow<Int?> = _cartId.asStateFlow()

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
            _fireId.value = it
        }
    }

    fun setOrderDetails(fireId: String?, deliveryAddress: String) {
        _fireId.value = deliveryAddress
        _deliveryAddress.value = deliveryAddress
    }

    fun confirmOrder(
        userId: Int?,
        paymentMethod: PaymentMethod,
        fireId: String?,
        deliveryAddress: String?,
        onFailure: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onFailure(IllegalStateException("User not authenticated."))
            return
        }

        if (userId == null || cartId == null || deliveryAddress.isNullOrBlank()) {
            onFailure(IllegalStateException("Missing required order details (userId, cartId, or deliveryAddress)."))
            return
        }

        val newOrder = Order(
            fireId = fireId,
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