package com.githubsearcher.view.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.githubsearcher.model.Item

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindItem(item: Item)
}