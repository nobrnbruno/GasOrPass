package com.example.gasorpass

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gasorpass.ui.theme.GasOrPassTheme
import com.example.gasorpass.view.GasOrEthanol
import com.example.gasorpass.view.GasStationInput
import com.example.gasorpass.view.ListGasStations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val check: Boolean = loadCheck(this)
        setContent {
            GasOrPassTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "list",
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                        )
                    }) {
                    composable("list") { ListGasStations(navController) }
                    composable("input") { GasStationInput(navController) }
                    composable("gas_or_ethanol") { GasOrEthanol(navController, check) }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TESTE LOG", "On resume")
    }

    override fun onStart() {
        super.onStart()
        Log.d("TESTE LOG", "On start")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TESTE LOG", "On pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TESTE LOG", "On stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TESTE LOG", "On destroy")
    }

    private fun loadCheck(context: Context): Boolean {
        val sharedFileName = "settings_gas_or_eth"
        val sp: SharedPreferences = context.getSharedPreferences(sharedFileName, MODE_PRIVATE)
        val isChecked: Boolean = sp.getBoolean("isChecked", false)
        return isChecked
    }
}
