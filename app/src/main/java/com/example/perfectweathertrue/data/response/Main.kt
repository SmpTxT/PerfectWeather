package com.example.perfectweathertrue.data.response

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelslike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    @SerializedName("temp_max")
    val tempmax: Double,
    @SerializedName("temp_min")
    val tempmin: Double
)