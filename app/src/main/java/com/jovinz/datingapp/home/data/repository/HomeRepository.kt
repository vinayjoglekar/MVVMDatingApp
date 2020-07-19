package com.jovinz.datingapp.home.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovinz.datingapp.app.BaseApplication
import com.jovinz.datingapp.common.db.AppDatabase
import com.jovinz.datingapp.home.data.model.ProfilesResponse
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.network.ApiService
import com.jovinz.datingapp.network.BaseRepository
import com.jovinz.datingapp.network.Resource
import kotlinx.coroutines.Dispatchers
import java.io.IOException


class HomeRepository(val apiService: ApiService, val database: AppDatabase?) : BaseRepository() {

    suspend fun getProfiles(): Resource<ProfilesResponse> {
        return safeApiCallWithErrorData(Dispatchers.IO) { apiService.profiles() }
    }

    suspend fun loadJSONFromAsset(context: Context, fileName: String): Resource<ProfilesResponse>? {
        val resp: String
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            resp = String(buffer)

            val type = fromJson<ProfilesResponse>(resp)
            return safeApiCallWithErrorData(Dispatchers.IO) {
                type
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    private inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }

    suspend fun insertProfile(profile: ProfilesResponseItem) {
        database?.profilesDao()?.insert(profile)
    }

}