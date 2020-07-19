package com.jovinz.datingapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jovinz.datingapp.account.data.repository.LikedProfilesRepository
import com.jovinz.datingapp.account.viewmodel.LikedProfilesViewModel
import com.jovinz.datingapp.app.BaseApplication
import com.jovinz.datingapp.home.data.repository.HomeRepository
import com.jovinz.datingapp.home.viewmodel.HomeViewModel
import com.jovinz.datingapp.network.ApiService

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return BaseApplication.instance?.let {
                HomeViewModel(
                    HomeRepository(
                        apiService = apiService, database = BaseApplication.appDatabase
                    ), LikedProfilesRepository(
                        database = BaseApplication.appDatabase
                    ), application = it
                )
            } as T
        } else if (modelClass.isAssignableFrom(LikedProfilesViewModel::class.java)) {
            return BaseApplication.instance?.let {
                LikedProfilesViewModel(
                    LikedProfilesRepository(
                        database = BaseApplication.appDatabase
                    )
                )
            } as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
