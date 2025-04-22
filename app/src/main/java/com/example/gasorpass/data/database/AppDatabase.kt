package com.example.gasorpass.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gasorpass.data.model.GasStation

@Database(entities = [GasStation::class], version = 1, exportSchema = false)
abstract class GasDatabase : RoomDatabase() {
    abstract fun gasStationDao(): GasStationDao

    companion object {
        @Volatile
        private var INSTANCE: GasDatabase? = null

        fun getDatabase(context: Context): GasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GasDatabase::class.java,
                    "gas_station_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}