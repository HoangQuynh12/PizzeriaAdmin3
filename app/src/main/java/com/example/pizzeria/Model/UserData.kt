package com.example.pizzeria.Model


data class UserData(
    var userID: String?="",
    var fullName: String?="",
    var phoneNumber: String?="",
    var email: String?="",
    var password: String?="",
    var address: String? = null,
    var sex: String?=null,
    var birthday: String? = null,
    var image: String?="",
    var role: String?=""
)
