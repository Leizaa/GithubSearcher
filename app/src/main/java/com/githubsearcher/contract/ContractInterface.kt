package com.githubsearcher.contract

import android.widget.EditText
import com.githubsearcher.view.holder.BaseViewHolder
import com.githubsearcher.model.Item

interface ContractInterface {

    interface View {
        fun init()
        fun initSearchComponent()
        fun processNoData()
        fun processErrorCall()
        fun hideKeyboard()
        fun hideErrorTextView()
        fun notifyDataChange()
        fun initiateAdapter(usernames: MutableList<Item>)
        fun dismissProgressBar()
        fun showProgressBar()
        fun addPagingProgressBar()
        fun removePagingProgressBar()
        fun showToast(message: String)
    }

    interface Presenter {
        fun handleOnClick(searchEditText: EditText)
        fun getNextPage()
        fun getItemCount() : Int
        fun bindViewHolder(holder: BaseViewHolder, position: Int)
        fun haveMoreData() : Boolean
        fun addLoading()
        fun removeLoading()
    }

    interface Model {

    }
}