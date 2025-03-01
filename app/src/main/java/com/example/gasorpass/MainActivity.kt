package com.example.gasorpass

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gasorpass.ui.theme.GasOrPassTheme
import com.example.gasorpass.view.GasOrEthanol
import com.example.gasorpass.view.GasStationInput
import com.example.gasorpass.view.ListofGasStations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var check= false
        check=loadCheck(this)
        setContent {
            GasOrPassTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") { ListofGasStations(navController) }
                    composable("input") { GasStationInput(navController) }
                    composable("gas_or_ethanol") { GasOrEthanol(navController, check) }
//                    composable("listaDePostos/{posto}") { backStackEntry ->
//                        val posto = backStackEntry.arguments?.getString("posto") ?: ""
//                        ListofGasStations(navController, posto)
//                    }
                }
            }
        }
    }

    private fun loadCheck(context: Context):Boolean{
        val sharedFileName = "settings_gas_or_eth"
        val sp: SharedPreferences = context.getSharedPreferences(sharedFileName, MODE_PRIVATE)
        var isChecked = false
        isChecked = sp.getBoolean("isChecked", false)
        return isChecked
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
