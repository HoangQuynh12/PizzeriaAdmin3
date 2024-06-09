package com.example.pizzeria.Model

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Order(
    var id: String = "",
    val custumerid: String? = "",
    val custumerName: String? = "",
    val custumerPhone: String? = "",
    val custumerAdd: String? = "",
    val date: Date? = null,
    val total: Double? = 0.0,
    val restaurant: String? = "",
    var status: String? = "",
    var items: List<OrderItem>? = emptyList()
)

