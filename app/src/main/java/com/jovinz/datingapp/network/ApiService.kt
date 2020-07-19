package com.jovinz.datingapp.network


import com.jovinz.datingapp.home.data.model.ProfilesResponse
import retrofit2.http.GET

interface ApiService {
    @GET("v3/e5f9bc00-a641-430c-9160-2469a6c1ab6b")
    suspend fun profiles(): ProfilesResponse

}