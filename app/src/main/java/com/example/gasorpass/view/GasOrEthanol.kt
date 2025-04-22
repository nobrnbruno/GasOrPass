package com.example.gasorpass.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gasorpass.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasOrEthanol(navController: NavHostController, check:Boolean) {
    val context = LocalContext.current

    var textFieldGas: String by rememberSaveable { mutableStateOf("") }
    var textFieldEthanol: String by rememberSaveable { mutableStateOf("") }

    var checkedState: Boolean by rememberSaveable { mutableStateOf(check) }
    var percentage: Double by rememberSaveable { mutableDoubleStateOf(1.0) }

    var resultMessage: String by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.description_return)
                        )
                    }
                },
                title = {Text("") },
                actions = {},
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Column (
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.body_gas_or_pass) + ":",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                )
                TextFields(
                    textFieldGas = textFieldGas,
                    textFieldEthanol = textFieldEthanol,
                    onGasChange = { textFieldGas = it },
                    onEthanolChange = { textFieldEthanol = it }
                )
                Column (
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.efficiency_message),
                        fontSize = 16.sp
                    )
                    Row(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "70%", fontSize = 16.sp)
                        Switch(
                            checked = checkedState,
                            onCheckedChange = {
                                checkedState = it
                                percentage = if (it) 0.75 else 0.7
                                saveChecked(context, checkedState)
                            }
                        )
                        Text(text = "75%", fontSize = 16.sp)
                    }
                }
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
                            resultMessage = ""
                        }
                    ) {
                        Text(text = stringResource(R.string.clear_action))
                    }
                    Button(
                        onClick = {
                            val gasPrice = textFieldGas.replace(",", ".").toDoubleOrNull()
                            val ethanolPrice = textFieldEthanol.replace(",", ".").toDoubleOrNull()
                            resultMessage = if (gasPrice != null && ethanolPrice != null) {
                                (if (ethanolPrice <= gasPrice * percentage)
                                    context.getString(R.string.result_ethanol) else context.getString(R.string.result_gas))
                            } else {
                                context.getString(R.string.result_repeat)
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.decide_action))
                    }
                }
            }
            Text(
                text = resultMessage,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
            )
        }
    }
}

@Composable
fun TextFields(
    textFieldGas: String,
    textFieldEthanol: String,
    onGasChange: (String) -> Unit,
    onEthanolChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            singleLine = true,
            value = textFieldGas,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                onGasChange(formatDecimals(formattedPoint))
            },
            label = { Text(stringResource(R.string.input_gas_liter)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text(text = "R\$", fontWeight = FontWeight.Bold) },
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
            prefix = { Text(text = "R\$", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun formatDecimals(input: String): String {
    val parts = input.split(",")
    return when (parts.size) {
        1 -> input
        2 -> parts[0] + "," + parts[1].take(2)
        else -> parts[0] + "," + parts[1].take(2)
    }
}

fun saveChecked(context: Context, switchState:Boolean){
    val sharedFileName = "settings_gas_or_eth"
    val sp: SharedPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.putBoolean("isChecked", switchState)
    editor.apply()
}
