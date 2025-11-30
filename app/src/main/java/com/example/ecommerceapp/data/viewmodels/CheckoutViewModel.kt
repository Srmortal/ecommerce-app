package com.example.ecommerceapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.Order
import com.example.ecommerceapp.data.models.OrderStatus
import com.example.ecommerceapp.data.models.PaymentMethod
import com.example.ecommerceapp.presentation.checkout.UserBillingData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class CheckoutViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val _uiState = MutableStateFlow(UserBillingData())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchUserData()
    }

    fun confirmOrder(
        paymentMethod: PaymentMethod,
        fireId: String,
        deliveryAddress: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val newOrder = Order(
            paymentMethod = paymentMethod,
            orderDate = Date(),
            status = OrderStatus.PENDING,
            deliveryAddress = deliveryAddress,
            fireId = fireId
        )

        db.collection("users")
            .document(fireId)
            .collection("orders")
            .add(newOrder)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }

    }

    private fun fetchUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = auth.currentUser?.uid

            if (userId != null) {
                try {
                    val document = db.collection("users").document(userId).get().await()
                    if (document.exists()) {
                        val data = document.toObject(UserBillingData::class.java)
                        data?.let { _uiState.value = it }
                    }
                } catch (e: Exception) {
                    Log.e("BillingVM", "Error fetching data", e)
                }
            } else {
                _uiState.value = UserBillingData(
                    fullName = "SUHA JANNAT",
                    email = "care@example.com",
                    phone = "+880 000 111 222",
                    address = "28/C Green Road, BD"
                )
            }
            _isLoading.value = false
        }
    }
}