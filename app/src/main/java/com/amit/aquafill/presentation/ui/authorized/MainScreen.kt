package com.amit.aquafill.presentation.ui.authorized

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavigationDrawer(navController)
    NavHost(navController = navController, startDestination = "entries") {
        composable("entries") {
            ManageEntitiesScreen()
        }
        composable("customer") {
            ManageCustomerScreen()
        }
    }
}