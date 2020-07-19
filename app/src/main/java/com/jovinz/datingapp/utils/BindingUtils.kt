package com.jovinz.datingapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("image")
fun setImage(image: ImageView, url: String?) = Glide.with(image.context).load(url)
    .transition(DrawableTransitionOptions.withCrossFade()).into(image)


@BindingAdapter("circularImage")
fun setCircularImage(image: ImageView, url: String?) =
    Glide.with(image.context).load(url).apply(
        RequestOptions().circleCrop()
    ).transition(DrawableTransitionOptions.withCrossFade()).into(image)
