package com.jovinz.datingapp.common.preferences

import android.content.Context
import android.content.SharedPreferences
import com.jovinz.datingapp.utils.Constants

object UserPrefs {

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(Constants.PREFS_FILE_NAME, MODE)
    }

    fun saveString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    fun saveBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun clearAllData() {
        preferences.edit().clear().apply()
    }

    fun removeString(key: String) {
        preferences.edit().remove(key).apply()
    }

}