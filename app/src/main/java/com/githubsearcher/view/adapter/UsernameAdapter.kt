package com.githubsearcher.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.githubsearcher.R
import com.githubsearcher.contract.ContractInterface.*
import com.githubsearcher.view.holder.BaseViewHolder
import com.githubsearcher.view.holder.LoadingHolder
import com.githubsearcher.view.holder.UsernameHolder

class UsernameAdapter(private val mainPresenter: Presenter) : RecyclerView.Adapter<BaseViewHolder>() {
    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoading : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == VIEW_TYPE_LOADING) {
            return LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fetching_item_view, parent, false)
            )
        } else {
            return UsernameHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.username_item_view, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        mainPresenter.bindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return mainPresenter.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoading) {
            return if(position == itemCount - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            return VIEW_TYPE_NORMAL
        }
    }

    fun setIsLoading(isLoading : Boolean) {
        this.isLoading = isLoading
    }

    fun isLoading(): Boolean {
        return isLoading
    }
}