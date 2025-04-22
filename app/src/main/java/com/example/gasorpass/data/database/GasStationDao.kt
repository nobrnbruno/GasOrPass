package com.example.gasorpass.data.database

import androidx.room.*
import com.example.gasorpass.data.model.GasStation
import kotlinx.coroutines.flow.Flow

@Dao
interface GasStationDao {
    @Query("SELECT * FROM gas_station")
    fun getAllStations(): Flow<List<GasStation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: GasStation): Long

    @Update
    suspend fun updateStation(station: GasStation)

    @Delete
    suspend fun deleteStation(station: GasStation)

    @Query("SELECT * FROM gas_station WHERE name = :name")
    suspend fun getStationByName(name: String): GasStation?
}