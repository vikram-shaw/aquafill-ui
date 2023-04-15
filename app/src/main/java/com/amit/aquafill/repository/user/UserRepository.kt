package com.amit.aquafill.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amit.aquafill.domain.model.User
import com.amit.aquafill.network.UserService
import com.amit.aquafill.network.response.UserResponse
import com.amit.aquafill.network.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService): IUserRepository {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    override suspend fun signup(email: String, password: String) {
        val response = userService.signup(user = User(email, password))
        handleResponse(response)
    }
    override suspend fun reset(email: String, password: String) {
        val response = userService.reset(user = User(email, password))
        handleResponse(response)
    }

    override suspend fun signing(email: String, password: String) {
        val response = userService.signing(user = User(email, password))
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if(response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody() != null) {
            val errObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errObj.getString("message")))
        }
        else {
            _userResponseLiveData.postValue(NetworkResult.Error("something went wrong"))
        }
    }

}