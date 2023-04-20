package com.amit.aquafill.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amit.aquafill.domain.model.User
import com.amit.aquafill.network.UserAuthService
import com.amit.aquafill.network.UserService
import com.amit.aquafill.network.response.UserInfoResponse
import com.amit.aquafill.network.response.UserResponse
import com.amit.aquafill.network.util.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService,
    private val userAuthService: UserAuthService): IUserRepository {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    override suspend fun signup(email: String, password: String): NetworkResult<UserResponse> {
        return try {
            val response = userService.signup(user = User(email, password))
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong...")
            }
        } catch (e: Exception) {
            print(e.message)
            NetworkResult.Error("Please try after sometime...")
        }
    }
    override suspend fun reset(email: String, password: String): NetworkResult<UserResponse> {
        return try {
            val response = userService.reset(user = User(email, password))
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong...")
            }
        } catch (e: Exception) {
            print(e.message)
            NetworkResult.Error("Please try after sometime...")
        }
    }

    override suspend fun signing(email: String, password: String): NetworkResult<UserResponse> {
        return try {
            val response = userService.signing(user = User(email, password))
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("Something went wrong...")
            }
        } catch (e: Exception) {
            print(e.message)
            NetworkResult.Error("Please try after sometime...")
        }
    }

    override suspend fun user(): NetworkResult<UserInfoResponse> {
        return try {
            val response = userAuthService.user()
            if(response.isSuccessful) {
                NetworkResult.Success(
                    data = response.body()!!
                )
            } else {
                NetworkResult.Error("not success")
            }
        } catch(e: Exception) {
            print(e.message)
            NetworkResult.Error("Something went wrong")
        }
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