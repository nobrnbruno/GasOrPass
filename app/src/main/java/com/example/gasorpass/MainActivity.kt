package com.example.gasorpass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gasorpass.ui.theme.GasOrPassTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GasOrPassTheme {
                HomeScaffold()
            }
        }
    }
    override fun onResume(){
        super.onResume()
        Log.d("TESTE LOG","On resume")
    }
    override fun onStart(){
        super.onStart()
        Log.d("TESTE LOG","On start")
    }
    override fun onPause(){
        super.onPause()
        Log.d("TESTE LOG","On pause")
    }
    override fun onStop(){
        super.onStop()
        Log.d("TESTE LOG","On stop")
    }
    override fun onDestroy(){
        super.onDestroy()
        Log.d("TESTE LOG","On destroy")
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScaffold() {
    var textFieldGas: String by rememberSaveable { mutableStateOf("") }
    var textFieldEthanol: String by rememberSaveable { mutableStateOf("") }
    var checked: Boolean by rememberSaveable { mutableStateOf(true) }
    var resultMessage: String by rememberSaveable { mutableStateOf("") }
    val percentage: Double by rememberSaveable { mutableDoubleStateOf(if (checked) 0.75 else 0.7) }
    Log.d("SWITCH", "Porcentagem selecionada: ${percentage*100}%")

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFields(
                textFieldGas = textFieldGas,
                textFieldEthanol = textFieldEthanol,
                onGasChange = { textFieldGas = it },
                onEthanolChange = { textFieldEthanol = it }
            )
            SwitchGasOption(
                checked = checked,
                onCheckedChange = { checked = it }
            )
            ButtonCalculate(
                onCalculate = {
                    val gasPrice = textFieldGas.replace(",", ".").toDoubleOrNull()
                    val ethanolPrice = textFieldEthanol.replace(",", ".").toDoubleOrNull()
                    resultMessage = if (gasPrice != null && ethanolPrice != null) {
                        (if (ethanolPrice <= gasPrice * percentage) "Etanol vale mais a pena!" else "Melhor usar gasolina mesmo...")
                    } else {
                        "Insira valores válidos."
                    }
                }
            )
            Text(
                text = resultMessage,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
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
            value = textFieldGas,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                onGasChange(formatDecimals(formattedPoint))
            },
            label = { Text("Litro da gasolina") },
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
            label = { Text("Litro do etanol") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text(text = "R\$", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SwitchGasOption(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .wrapContentSize(Alignment.TopCenter),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "70%", fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = "75%", fontSize = 16.sp)
    }
}

@Composable
fun ButtonCalculate(onCalculate: () -> Unit) {
    Button(
        onClick = onCalculate,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Decidir")
    }
}
