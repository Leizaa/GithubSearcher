package com.githubsearcher.presenter

import android.app.Activity
import android.util.Log
import android.widget.EditText
import com.githubsearcher.view.holder.BaseViewHolder
import com.githubsearcher.utility.GithubService
import com.githubsearcher.utility.GithubServiceBuilder
import com.githubsearcher.R
import com.githubsearcher.contract.ContractInterface.*
import com.githubsearcher.model.Item
import com.githubsearcher.model.ResultData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityPresenter(private var view: View, private val context: Activity) : Presenter {
    private lateinit var items: MutableList<Item>
    private var page: Int = 1
    private var haveMoreData: Boolean = true
    private var userToBeSearch : String = ""

    init {
        view.init()
        view.initSearchComponent()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun bindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun handleOnClick(searchEditText: EditText) {
        if(searchEditText.text.isEmpty()) {
            searchEditText.error = context.getString(R.string.empty_user_name_error)
        } else {
            view.hideKeyboard()
            view.hideErrorTextView()
            view.showProgressBar()
            userToBeSearch = searchEditText.text.toString()
            val query = context.getString(R.string.username_query, userToBeSearch)
            page = 1
            beginSearch(query, page)
        }
    }

    override fun addLoading() {
        items.add(Item("",""))
    }

    override fun removeLoading() {
        items.removeAt(items.size - 1)
    }

    override fun haveMoreData(): Boolean {
        return haveMoreData
    }

    private fun beginSearch(searchString: String, page: Int) {
        val request = GithubServiceBuilder.buildService(GithubService::class.java)
        val call = request.getUsernameList(searchString, 30,page)

        Log.v("GET", "begin search: $searchString")
        call.enqueue(object : Callback<ResultData> {
            override fun onResponse(call: Call<ResultData>, response: Response<ResultData>) {
                if (response.isSuccessful) {
                    Log.v("GET", "url: " + response.raw().request().url())
                    Log.v("GET", "result: " + response.body().toString())
                    preProcessResponse(response)

                } else {
                    val errorJson = JSONObject(response.errorBody()!!.string())
                    Log.v("GET", "failed: $errorJson")
                    view.processErrorCall()
                }
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {

                if(page != 1) {
                    haveMoreData = false
                    view.removePagingProgressBar()
                    view.showToast(t.message.toString())
                } else {
                    view.processErrorCall()
                }
            }
        })
    }

    override fun getNextPage() {
        view.addPagingProgressBar()

        beginSearch(userToBeSearch, page)
    }

    private fun preProcessResponse(response: Response<ResultData>) {
        if(response.body()!!.items.isEmpty()) {
            view.processNoData()
        } else {
            processDataResponse(response)
        }
    }

    private fun processDataResponse(response: Response<ResultData>) {
        if(page == 1) {
            processFirstPage(response)
        } else {
            processNextPage(response)
        }
    }

    private fun processFirstPage(response: Response<ResultData>) {
        items = mutableListOf()
        items.addAll(response.body()!!.items)

        view.initiateAdapter(items)
        view.dismissProgressBar()
        checkMorePage(response.body()!!.totalCount)
    }

    private fun processNextPage(response: Response<ResultData>) {
        view.removePagingProgressBar()
        items.addAll(response.body()!!.items)
        view.notifyDataChange()
        checkMorePage(response.body()!!.totalCount)
    }

    private fun checkMorePage(totalCount: Int) {
        haveMoreData = if(items.size < totalCount) {
            page++
            true
        } else {
            false
        }
    }
}