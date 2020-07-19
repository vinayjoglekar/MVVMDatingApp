package com.jovinz.datingapp.account.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jovinz.datingapp.account.data.repository.LikedProfilesRepository
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikedProfilesViewModel(private val likedProfilesRepository: LikedProfilesRepository) :
    ViewModel() {

    private val likedProfilesLiveData: MutableLiveData<Resource<List<ProfilesResponseItem>?>> =
        MutableLiveData()

    fun getLikedProfilesLiveData(): MutableLiveData<Resource<List<ProfilesResponseItem>?>> {
        return likedProfilesLiveData
    }

    fun getLikedProfiles() {
        likedProfilesLiveData.value = Resource.loading(null)
        var response: Resource<List<ProfilesResponseItem>?>?
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                response = likedProfilesRepository.getProfilesFromDb()
                likedProfilesLiveData.postValue(response)
            }
        }

    }
}