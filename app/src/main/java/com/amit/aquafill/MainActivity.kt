package com.amit.aquafill

import com.amit.aquafill.presentation.ui.authorized.MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amit.aquafill.presentation.ui.unauthorized.ForgetPassword
import com.amit.aquafill.presentation.ui.unauthorized.login.Login
import com.amit.aquafill.presentation.ui.unauthorized.Register
import com.amit.aquafill.ui.theme.AquaFillTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaFillTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable(route = "login") { Login(navController) }
                        composable(route = "register") { Register(navController) }
                        composable(route = "forget") { ForgetPassword(navController) }
                        composable(route = "main") { MainScreen() }
                    }
                }
            }
        }
    }
}
