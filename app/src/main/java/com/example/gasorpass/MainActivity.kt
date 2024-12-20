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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFields()
            SwitchGasOption()
            ButtonCalculate()
        }
    }
}


@Preview
@Composable
fun TextFields(modifier: Modifier = Modifier) {
    var textFieldGas by rememberSaveable { mutableStateOf("") }
    var textFieldEthanol by rememberSaveable { mutableStateOf("") }
    fun formatDecimals(input: String): String {
        val parts = input.split(",")
        return when (parts.size) {
            1 -> input
            2 -> parts[0] + "," + parts[1].take(3)
            else -> parts[0] + "," + parts[1].take(3)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = textFieldGas,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                textFieldGas = formatDecimals(formattedPoint)
            },
            label = { Text("Valor do litro da gasolina") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text(
                text = "R\$",
                fontWeight = FontWeight.Bold
            ) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = textFieldEthanol,
            onValueChange = { input ->
                val formattedPoint = input.replace(".", ",").filter { it.isDigit() || it == ',' }
                textFieldEthanol = formatDecimals(formattedPoint)
            },
            label = { Text("Valor do litro do etanol") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text(
                text = "R\$",
                fontWeight = FontWeight.Bold
            ) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun SwitchGasOption(modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(true) }

    Row (
        modifier = modifier
            .padding(24.dp)
            .wrapContentSize(Alignment.TopCenter),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text (
            text = "70%",
            fontSize = 16.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
        Text (
            text = "75%",
            fontSize = 16.sp
        )
    }
}


@Preview
@Composable
fun ButtonCalculate() {
    Button(
        onClick = ({}),
    ) {
        Text(
            text = "Calcular"
        )
    }
}