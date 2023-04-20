package com.amit.aquafill.presentation.ui.authorized

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.aquafill.network.util.NetworkResult
import com.amit.aquafill.repository.user.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NavigationDrawerUIState (
    val email: String = "",
    val name: String = "",
    val initialName: String = ""
)

@HiltViewModel
class NavigationDrawerViewModel @Inject constructor(
    private val userRepository: IUserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationDrawerUIState())
    val uiState: StateFlow<NavigationDrawerUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when(val response = userRepository.user()){
                is NetworkResult.Success -> {
                    val user = response.data!!
                    val initial = generateInitialByName(user.name)
                    _uiState.value = _uiState.value.copy(email = user.email, name = user.name, initialName = initial)
                }
                is NetworkResult.Error -> {
                    Log.d("user info", response.message.toString())
                }
                is NetworkResult.Loading -> {
                    Log.d("user info", response.toString())
                }
            }
        }
    }

    private fun generateInitialByName(name: String): String {
        val names = name.split(' ')
        var initial = ""
        names.forEach {
            initial += it[0]
        }
        return initial
    }
}