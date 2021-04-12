package com.githubsearcher.view.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.githubsearcher.R
import com.githubsearcher.model.Item
import com.squareup.picasso.Picasso

class UsernameHolder(view: View) : BaseViewHolder(view) {
    private val usernameTextView = view.findViewById<TextView>(R.id.user_text_view)
    private val imageView = view.findViewById<ImageView>(R.id.user_image_view)

    override fun bindItem(item: Item) {
        usernameTextView.text = item.loginName
        Picasso.get()
            .load(item.avatarUrl)
            .placeholder(R.drawable.default_image)
            .error(R.drawable.default_image)
            .into(imageView)
    }
}