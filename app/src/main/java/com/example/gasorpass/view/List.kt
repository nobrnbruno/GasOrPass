package com.example.gasorpass.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gasorpass.data.Coordinates
import com.example.gasorpass.data.GasStation
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListofGasStations(navController: NavHostController) {
    val context = LocalContext.current
    val gasStations = getListOfGasStationsJSON(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Lista de Postos") },
                actions = {
                    IconButton(onClick = { navController.navigate("gas_or_ethanol") }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "calcular"
                        )
                    }
                    IconButton(onClick = { navController.navigate("input") }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "adicionar"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(gasStations) { item ->
                GasStationCard(item, context)
            }
        }
    }
}

@Composable
fun GasStationCard(item: GasStation, context: Context) {
    Card(
        onClick = {
            val gmmIntentUri = Uri.parse("geo:${item.location.lat},${item.location.lgt}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            context.startActivity(mapIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Column {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row {
                    Text(text = "${item.location.lat}, ${item.location.lgt}", fontWeight = FontWeight.Normal)
                }
                Text(text = "Gasolina: R$ ${item.gasPrice} | Etanol: R$ ${item.ethanolPrice}")
            }
        }
    }
}

fun saveGasStationsJSON(context: Context, gasStation: GasStation) {
    val sharedFileName = "gasStationList"
    val sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    val editor = sp.edit()

    val jsonString = sp.getString("gasList", "[]") ?: "[]"
    val jsonArray = JSONArray(jsonString)

    val newGasJson = JSONObject().apply {
        put("name", gasStation.name)
        put("lat", gasStation.location.lat)
        put("lgt", gasStation.location.lgt)
        put("gasPrice", gasStation.gasPrice)
        put("ethanolPrice", gasStation.ethanolPrice)
    }

    jsonArray.put(newGasJson)

    editor.putString("gasList", jsonArray.toString())
    editor.apply()

    Log.d("PDM", "Saved Gas Station: $newGasJson")
}

fun getListOfGasStationsJSON(context: Context): List<GasStation> {
    val sharedFileName = "gasStationList"
    val sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)

    val jsonString = sp.getString("gasList", "[]") ?: "[]"
    val jsonArray = JSONArray(jsonString)

    val gasStationList = mutableListOf<GasStation>()

    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val name = jsonObject.getString("name")
        val lat = jsonObject.getDouble("lat")
        val lgt = jsonObject.getDouble("lgt")
        val gasPrice = jsonObject.getDouble("gasPrice")
        val ethanolPrice = jsonObject.getDouble("ethanolPrice")

        gasStationList.add(GasStation(name, Coordinates(lat, lgt), gasPrice, ethanolPrice))
    }

    return gasStationList
}

//Sugestão de métodos a serem usados para a versão final
//fun getListOfGasStation(context: Context):List<GasStation>{
//    val gasStation1 = GasStation("Posto na Espanha", Coordinates(41.40338, 2.17403))
//    val gasStation2 = GasStation("Posto em NY", Coordinates(40.7128, -74.0060))
//    val gasStation3= GasStation("Posto em Fortaleza",Coordinates(41.40338, 2.17403))
//
//    return listOf(gasStation1, gasStation2, gasStation3)
//}
//fun addGasStation(context: Context, gasStation: GasStation){
//
//}