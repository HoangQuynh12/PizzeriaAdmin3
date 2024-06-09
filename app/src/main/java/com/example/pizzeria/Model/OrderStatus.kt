package com.example.pizzeria.Model


enum class OrderStatus(code: String)  {
    Processing("Pending"),
    Shipping("Shipping"),
    Shipped("Shipped"),
    Canceled("Canceled"),
    Delivered("Delivered"),
}