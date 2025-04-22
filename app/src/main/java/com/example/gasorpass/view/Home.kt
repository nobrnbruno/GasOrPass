package com.example.gasorpass.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gasorpass.R
import com.example.gasorpass.data.model.Coordinates
import com.example.gasorpass.data.model.GasStation
import com.example.gasorpass.viewmodel.GasStationViewModel
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGasStations(
    navController: NavHostController,
    viewModel: GasStationViewModel = viewModel(),
) {
    val context = LocalContext.current
    val gasStations = viewModel.allGasStations.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("input") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.description_add))
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gas or Pass") },
                actions = {
                    IconButton(onClick = { navController.navigate("gas_or_ethanol") }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.description_calculate)
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
            contentPadding = PaddingValues(8.dp)
        ) {
            items(gasStations.value) { station ->
                GasStationCard(station, context)
            }
        }
    }
}

@Composable
fun GasStationCard(station: GasStation, context: Context) {
    Card(
        onClick = {
            val gmmIntentUri = "geo:${station.location.lat},${station.location.lgt}".toUri()
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            context.startActivity(mapIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = station.name,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row {
                    Text(
                        text = "${station.location.lat}, ${station.location.lgt}",
                        fontWeight = FontWeight.Normal
                    )
                }
                Text(
                    text = "${stringResource(R.string.card_gas)}: R$ ${station.gasPrice} | ${
                        stringResource(
                            R.string.card_ethanol
                        )
                    }: R$ ${station.ethanolPrice}"
                )
            }
        }
    }
}