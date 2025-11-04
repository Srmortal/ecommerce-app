package com.example.ecommerceapp.data.models

import java.util.Date

enum class OrderStatus(private val status: Pair<String, Int>) {
    PENDING("Pending" to 0),
    PROCESSING("Processing" to 1),
    SHIPPED("Shipped" to 2),
    DELIVERED("Delivered" to 3),
    CANCELLED("Cancelled" to 4);

    override fun toString(): String {
        return status.first
    }

    fun toInt(): Int {
        return status.second
    }
}

enum class PaymentMethod(private val method: Pair<String, Int>) {
    CASH_ON_DELIVERY("Cash on Delivery" to 0),
    CREDIT_CARD("Credit Card" to 1),
    BANK_TRANSFER("Bank Transfer" to 2);

    override fun toString(): String {
        return method.first
    }

    fun toInt(): Int {
        return method.second
    }
}

data class Order(
    val id: Int,
    val userId: Int,
    val cartId: Int,
    val paymentMethod: PaymentMethod,
    val orderDate: Date,
    val status: OrderStatus,
    val deliveryAddress: String,
    val fireId: String
)
