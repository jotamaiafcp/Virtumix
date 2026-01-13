package com.example.cookaplication_a043302.data.model

data class ShoppingItem(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val quantity: String? = null,
    val checked: Boolean = false
)
