package com.example.wearther.model

data class WeatherModel(
    val city: String,
    val time: String,
    val condition: String,
    val currentTemperature: String,
    val maxTemp: String,
    val minTemp: String,
    val imageURL: String,
    val hours: String
)
