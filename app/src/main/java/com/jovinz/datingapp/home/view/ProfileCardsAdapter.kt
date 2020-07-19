package com.jovinz.datingapp.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jovinz.datingapp.R
import com.jovinz.datingapp.databinding.ItemCardProfileBinding
import com.jovinz.datingapp.home.data.model.ProfilesResponseItem
import com.jovinz.datingapp.utils.GlideApp


class ProfileCardsAdapter : RecyclerView.Adapter<ProfileCardsAdapter.ProfileViewHolder>() {

    private var profiles: MutableList<ProfilesResponseItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProfileViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card_profile,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[holder.adapterPosition]
        holder.binding.profile = profile
        holder.binding.executePendingBindings()
    }

    fun setProfiles(profiles: MutableList<ProfilesResponseItem>) {
        val diffCallback = ProfilesDiffCallback(this.profiles, profiles)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.profiles.clear()
        this.profiles.addAll(profiles)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProfileViewHolder(val binding: ItemCardProfileBinding) :
        RecyclerView.ViewHolder(binding.root)


}

class ProfilesDiffCallback(
    private val oldList: List<ProfilesResponseItem>,
    private val newList: List<ProfilesResponseItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].email.equals(
            newList[newItemPosition].email,
            ignoreCase = true
        )
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].email.equals(
            newList[newPosition].email,
            ignoreCase = true
        ) && oldList[oldPosition].gender.equals(
            newList[newPosition].gender,
            ignoreCase = true
        )
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}