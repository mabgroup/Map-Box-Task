package com.mabdigital.core.base.adapters

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: AppCompatImageView, url: String?) {
    url?.run {
        Picasso.get()
            .load(this)
            .into(view)
    }
}