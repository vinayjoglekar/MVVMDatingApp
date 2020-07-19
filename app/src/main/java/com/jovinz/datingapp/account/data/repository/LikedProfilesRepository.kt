package com.jovinz.datingapp.account.data.repository

import com.jovinz.datingapp.common.db.AppDatabase
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.network.BaseRepository
import com.jovinz.datingapp.network.Resource
import kotlinx.coroutines.Dispatchers

class LikedProfilesRepository(val database: AppDatabase?) : BaseRepository() {

    suspend fun getProfilesFromDb(): Resource<List<ProfilesResponseItem>?> {
        return Resource.success(database?.profilesDao()?.getAllProfiles())
    }
}