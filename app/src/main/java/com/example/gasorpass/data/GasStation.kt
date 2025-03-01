package com.example.gasorpass.data

import java.io.Serializable

data class GasStation(
    var name: String,
    val location: Coordinates,
    val gasPrice: Double,
    val ethanolPrice: Double
): Serializable
