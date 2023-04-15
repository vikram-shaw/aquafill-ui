package com.amit.aquafill.presentation.ui.unauthorized.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
)

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val emailPattern: Regex = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")

    fun updateEmail(email: String) {
        val errors = mutableListOf<String>()
        if(!email.matches(emailPattern)) {
            errors.add("Enter valid email")
        }
        _uiState.value = _uiState.value.copy(
            currentEmail = email.trim(),
            currentEmailErrors = errors
        )
    }

    fun updatePassword(password: String) {
        val errors = mutableListOf<String>()
        if(password.trim().length < 6)
            errors.add("Please enter at least 6 characters.")

        _uiState.value = _uiState.value.copy(
            currentPassword = password.trim(),
            currentPasswordErrors = errors
        )
    }

    fun isValid(): Boolean {
        if(_uiState.value.currentEmail.isEmpty())
            return false
        if(_uiState.value.currentPassword.isEmpty())
            return false
        if(_uiState.value.currentPasswordErrors.isNotEmpty())
            return false
        if(_uiState.value.currentEmailErrors.isNotEmpty())
            return false
        return true
    }

    fun login() {
        viewModelScope.launch {
        }
    }
}
