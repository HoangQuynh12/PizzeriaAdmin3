package com.example.pizzeria.Model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: String? = "",
    val name: String? = "",
    val description: String? = "",
    val image: String? = "",
    val price: Double? = 0.0,
    val quantity: Int? = 0,
    val note: String? = ""
)