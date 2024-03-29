package com.example.wearther.model

data class DayItem(
    val city: String,
    val time: String,
    val weatherCondition: String,
    val imageUrl: String,
    val currentTemperature: String,
    val maxTemperature: String,
    val minTemperature: String,
    val hours: String
    )
