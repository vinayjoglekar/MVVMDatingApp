package com.jovinz.datingapp.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jovinz.datingapp.account.data.repository.LikedProfilesRepository
import com.jovinz.datingapp.common.preferences.UserPrefs
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.home.data.repository.HomeRepository
import com.jovinz.datingapp.network.Resource
import com.jovinz.datingapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val likedProfilesRepository: LikedProfilesRepository,
    application: Application
) :
    AndroidViewModel(application) {

    private val profilesLiveData: MutableLiveData<Resource<List<ProfilesResponseItem>>> =
        MutableLiveData()

    fun getProfilesLiveData(): MutableLiveData<Resource<List<ProfilesResponseItem>>> {
        return profilesLiveData
    }

    fun getProfiles() {
        profilesLiveData.value = Resource.loading(null)
        var response: Resource<List<ProfilesResponseItem>>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val likedProfiles = async {
                    likedProfilesRepository.getProfilesFromDb()
                }.await()

                val profiles = async {
                    homeRepository.getProfiles()
                }.await()

                profiles.data?.let { profilesList ->
                    if (likedProfiles.data.isNullOrEmpty()) {
                        profilesLiveData.postValue(Resource.success(profilesList))
                    } else {
                        response = Resource.success(
                            getFilteredList(
                                profilesList,
                                likedProfiles.data
                            )
                        )
                        profilesLiveData.postValue(response)
                    }
                }
            }
        }
    }

    fun getProfilesFromLocal() {
        profilesLiveData.value = Resource.loading(null)
        var response: Resource<List<ProfilesResponseItem>>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val likedProfiles = async {
                    likedProfilesRepository.getProfilesFromDb()
                }.await()

                val profiles = async {
                    homeRepository.loadJSONFromAsset(
                        getApplication(),
                        "home.json"
                    )
                }.await()

                profiles?.data?.let { profilesList ->
                    if (likedProfiles.data.isNullOrEmpty()) {
                        profilesLiveData.postValue(Resource.success(profilesList))
                    } else {
                        response = Resource.success(
                            getFilteredList(
                                profilesList,
                                likedProfiles.data
                            )
                        )
                        profilesLiveData.postValue(response)
                    }
                }
            }
        }
    }

    fun insertProfileIntoDb(profile: ProfilesResponseItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                homeRepository.insertProfile(profile)
            }
        }
    }

    private fun getFilteredList(
        data: List<ProfilesResponseItem>,
        likedProfilesList: List<ProfilesResponseItem>
    ): List<ProfilesResponseItem> {
        return data.filter { item ->
            !UserPrefs.getString(Constants.GENDER).equals(item.gender, true)
                    && !likedProfilesList.contains(item)
        }
    }
}