package com.amit.aquafill.utils

import android.content.Context
import android.content.SharedPreferences
import com.amit.aquafill.network.model.UserDto
import com.amit.aquafill.utils.Constants.USER
import com.amit.aquafill.utils.Constants.USER_TOKEN
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("prefs_token", Context.MODE_PRIVATE)

    fun save(token: String, user: UserDto) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER, user.toString())
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getUserInfo(): UserDto {
        return Gson().fromJson(prefs.getString(USER, null)!!, UserDto::class.java)
    }
}