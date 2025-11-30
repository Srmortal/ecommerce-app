package com.example.ecommerceapp.data.models

import java.math.BigDecimal

data class BillingDetails(
    val fullName: String,
    val emailAddress: String,
    val phoneNumber: String,
    val shippingAddress: String
)

data class ShippingOption(
    val id: String,
    val name: String,
    val deliveryTime: String,
    val price: BigDecimal
)