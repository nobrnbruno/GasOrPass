package com.example.gasorpass.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gas_station")
data class GasStation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @Embedded val location: Coordinates,
    val gasPrice: Double,
    val ethanolPrice: Double
)