package com.giaquino.cm.common.util

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:image_url")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrBlank()) {
            Picasso.with(view.context).load(url).into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}