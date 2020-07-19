package com.jovinz.datingapp.account.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jovinz.datingapp.R
import com.jovinz.datingapp.account.viewmodel.LikedProfilesViewModel
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.network.Resource
import com.jovinz.datingapp.network.RetrofitBuilder
import com.jovinz.datingapp.utils.ViewModelFactory
import com.jovinz.datingapp.utils.gone
import com.jovinz.datingapp.utils.visible
import kotlinx.android.synthetic.main.fragment_liked_profiles.*

class LikedProfilesFragment : Fragment(R.layout.fragment_liked_profiles) {

    private val likedProfilesViewModel: LikedProfilesViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory((RetrofitBuilder.apiService))
        ).get(LikedProfilesViewModel::class.java)
    }

    private val likedProfilesAdapter: LikedProfilesAdapter by lazy { LikedProfilesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerviewLikedProfiles.adapter = likedProfilesAdapter
        observeData()
        likedProfilesViewModel.getLikedProfiles()
    }

    private fun observeData() {
        likedProfilesViewModel.getLikedProfilesLiveData()
            .observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    Resource.Status.LOADING -> {
                        progressLikedProfiles.visible()
                        tvNoData.gone()
                    }
                    Resource.Status.ERROR -> {
                        progressLikedProfiles.gone()
                        tvNoData.visible()
                    }
                    Resource.Status.SUCCESS -> {
                        progressLikedProfiles.gone()
                        if (response.data.isNullOrEmpty()) {
                            tvNoData.visible()
                        } else {
                            tvNoData.gone()
                            likedProfilesAdapter.setProfiles(response.data as MutableList<ProfilesResponseItem>)
                        }
                    }
                }
            })
    }
}