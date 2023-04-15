package com.amit.aquafill.utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract.Constants
import com.amit.aquafill.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("prefs_token", Context.MODE_PRIVATE)

    fun save(token: String) {
        var editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}