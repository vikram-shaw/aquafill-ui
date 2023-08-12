package com.amit.aquafill.presentation.ui.unauthorized.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.user.IUserRepository
import com.amit.aquafill.routes.Routes
import com.amit.aquafill.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val currentEmail: String = "",
    val currentEmailErrors: MutableList<String> = mutableListOf(),
    val currentPassword: String = "",
    val currentPasswordErrors: MutableList<String> = mutableListOf(),
    val loading: MutableState<Boolean> = mutableStateOf(false),
    val valid: MutableState<Boolean> = mutableStateOf(false)
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: IUserRepository,
    private val tokenManager: TokenManager): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val emailPattern: Regex = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]")
    fun redirectIfLoggedIn(navController: NavController) {
        if(tokenManager.getToken() != null) {
            navController.navigate(Routes.Main.name) {
                popUpTo(Routes.Login.name) {
                    inclusive = true
                }
            }
        }
    }
    fun updateEmail(email: String) {
        val errors = mutableListOf<String>()
        val isEmailValid = if(!email.matches(emailPattern)) {
            Log.v("Tag",email)
            errors.add("Enter valid email")
            false
        } else {
            errors.clear()
            true
        }
        _uiState.value = _uiState.value.copy(
            currentEmail = email.trim(),
            currentEmailErrors = errors,
        )
        _uiState.value.valid.value = isEmailValid
    }

    fun updatePassword(password: String) {
        val errors = mutableListOf<String>()
        val isPasswordValid = if(password.trim().length < 6) {
            errors.add("Please enter at least 6 characters.")
            false
        } else {
            true
        }

        _uiState.value = _uiState.value.copy(
            currentPassword = password.trim(),
            currentPasswordErrors = errors,
        )
        _uiState.value.valid.value = isPasswordValid
    }

    fun login(navController: NavHostController, context: Context) {
        uiState.value.loading.value = true
        viewModelScope.launch {
            when(val response = userRepository.signing(_uiState.value.currentEmail, _uiState.value.currentPassword)) {
                is NetworkResult.Success -> {
                    tokenManager.save(response.data!!.token, response.data.user)
                    uiState.value.loading.value = false
                    redirectIfLoggedIn(navController)
                }
                else -> {
                    uiState.value.loading.value = false
                    Log.v("Error", response.message!!)
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
