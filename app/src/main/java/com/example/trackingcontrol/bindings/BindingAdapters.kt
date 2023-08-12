package com.example.trackingcontrol.bindings

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.trackingcontrol.models.UserData
import com.example.trackingcontrol.utils.Resource

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .signature(ObjectKey(url.hashCode()))
            .into(imageView)
    }
}


