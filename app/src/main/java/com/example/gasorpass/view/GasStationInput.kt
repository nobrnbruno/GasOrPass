package com.example.gasorpass.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gasorpass.R
import com.example.gasorpass.data.model.Coordinates
import com.example.gasorpass.data.model.GasStation
import com.example.gasorpass.viewmodel.GasStationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasStationInput(navController: NavHostController, viewModel: GasStationViewModel = viewModel()) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var latitude by remember { mutableStateOf("...") }
    var longitude by remember { mutableStateOf("...") }

    var textFieldGas: String by rememberSaveable { mutableStateOf("") }
    var textFieldEthanol: String by rememberSaveable { mutableStateOf("") }
    var textFieldName: String by rememberSaveable { mutableStateOf("") }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation(context, fusedLocationClient) { lat, lng ->
                latitude = lat.toString()
                longitude = lng.toString()
            }
        } else {
            latitude = "Permission Denied"
            longitude = "Permission Denied"
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(context, fusedLocationClient) { lat, lng ->
                latitude = lat.toString()
                longitude = lng.toString()
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.description_return)
                        )
                    }
                },
                title = {Text(stringResource(R.string.gas_station)) }
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            InputTextFields(
                latitude = latitude,
                longitude = longitude,
                textFieldGas = textFieldGas,
                textFieldEthanol = textFieldEthanol,
                textFieldName = textFieldName,
                onGasChange = { textFieldGas = it },
                onEthanolChange = { textFieldEthanol = it },
                onNameChange = { textFieldName = it }
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.CenterEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = {
                        textFieldGas = ""
                        textFieldEthanol = ""
                        textFieldName = "" },
                ) {
                    Text(text = stringResource(R.string.clear_action))
                }
                Button(
                    onClick = {
                            if (textFieldName.isNotBlank() && textFieldGas.isNotBlank() && textFieldEthanol.isNotBlank()) {
                                try {
                                    val gasPrice = textFieldGas.replace(",", ".").toDouble()
                                    val ethanolPrice = textFieldEthanol.replace(",", ".").toDouble()
                                    val latitudeValue = latitude.toDoubleOrNull() ?: 0.0
                                    val longitudeValue = longitude.toDoubleOrNull() ?: 0.0

                                    viewModel.insertGasStation(
                                        name = textFieldName,
                                        lat = latitudeValue,
                                        lng = longitudeValue,
                                        gasPrice = gasPrice,
                                        ethanolPrice = ethanolPrice
                                    )

                                    textFieldGas = ""
                                    textFieldEthanol = ""
                                    textFieldName = ""

                                    navController.navigate("list")
                                } catch (_: NumberFormatException) {
                                    Toast.makeText(context,
                                        context.getString(R.string.warning_prices), Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.warning_inputs), Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                ) {
                    Text(text = stringResource(R.string.save_action))
                }
            }
        }
    }
}

@Composable
fun InputTextFields(
    latitude: String,
    longitude: String,
    textFieldName: String,
    textFieldGas: String,
    textFieldEthanol: String,
    onGasChange: (String) -> Unit,
    onEthanolChange: (String) -> Unit,
    onNameChange: (String) -> Unit
) {
    fun formatDecimals(input: String): String {
        val parts = input.split(",")
        return when (parts.size) {
            1 -> input
            2 -> parts[0] + "," + parts[1].take(2)
            else -> parts[0] + "," + parts[1].take(2)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            singleLine = true,
            value = textFieldName,
            label = { Text(stringResource(R.string.input_gas_station)) },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onNameChange
        )
        OutlinedTextField(
            singleLine = true,
            value = textFieldGas,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                onGasChange(formatDecimals(formattedPoint))
            },
            label = { Text(stringResource(R.string.input_gas_liter)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text(text = "R$", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            singleLine = true,
            value = textFieldEthanol,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                onEthanolChange(formatDecimals(formattedPoint))
            },
            label = { Text(stringResource(R.string.input_ethanol_liter)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            prefix = { Text(text = "R$", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth()
        )
        Box(Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Column {
                Text(
                    text = stringResource(R.string.location),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row {
                    Text(
                        text = latitude,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = ", ",
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = longitude,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

private fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Double, Double) -> Unit
) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocationReceived(it.latitude, it.longitude)
            }
        }
    }
}