package com.example.gasorpass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gasorpass.data.database.GasDatabase
import com.example.gasorpass.data.model.Coordinates
import com.example.gasorpass.data.model.GasStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GasStationViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = GasDatabase.getDatabase(application).gasStationDao()

    val allGasStations: Flow<List<GasStation>> = dao.getAllStations()

    fun insertGasStation(name: String, lat: Double, lng: Double, gasPrice: Double, ethanolPrice: Double) {
        viewModelScope.launch {
            val gasStation = GasStation(
                name = name,
                location = Coordinates(lat, lng),
                gasPrice = gasPrice,
                ethanolPrice = ethanolPrice
            )
            dao.insertStation(gasStation)
        }
    }

    fun updateGasStation(station: GasStation) {
        viewModelScope.launch {
            dao.updateStation(station)
        }
    }

    fun deleteGasStation(station: GasStation) {
        viewModelScope.launch {
            dao.deleteStation(station)
        }
    }
}