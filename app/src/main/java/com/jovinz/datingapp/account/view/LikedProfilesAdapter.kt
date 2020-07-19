package com.jovinz.datingapp.account.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jovinz.datingapp.R
import com.jovinz.datingapp.databinding.ItemLikedProfilesBinding
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem

class LikedProfilesAdapter : RecyclerView.Adapter<LikedProfilesAdapter.LikedProfilesViewHolder>() {

    private var profiles: MutableList<ProfilesResponseItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LikedProfilesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_liked_profiles,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: LikedProfilesViewHolder, position: Int) {
        val profile = profiles[holder.adapterPosition]
        holder.binding.profile = profile
        holder.binding.executePendingBindings()
    }

    fun setProfiles(profiles: MutableList<ProfilesResponseItem>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }

    inner class LikedProfilesViewHolder(val binding: ItemLikedProfilesBinding) :
        RecyclerView.ViewHolder(binding.root)

}