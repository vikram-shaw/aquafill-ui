package com.amit.aquafill.presentation.ui.authorized

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.amit.aquafill.routes.Routes
import com.amit.aquafill.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val tokenManager: TokenManager): ViewModel() {
    fun redirectIfLoggedIn(navController: NavController) {
        if(tokenManager.getToken() == null) {
            navController.navigate(Routes.Login.name)
        }
    }
}