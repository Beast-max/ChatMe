package com.example.chatme

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

 class MessageModel (
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var date: String = "",
    var type: String = ""

) { companion object {

    @JvmStatic
    @BindingAdapter("imageMessage")
    public fun loadImage(imageView: ImageView, image: String?) {
        if (image != null) {
            Glide.with(imageView.context).load(image).into(imageView)
        }
    }
}
}