package com.githubsearcher.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.githubsearcher.*
import com.githubsearcher.contract.ContractInterface
import com.githubsearcher.model.Item
import com.githubsearcher.presenter.MainActivityPresenter
import com.githubsearcher.view.adapter.UsernameAdapter


class MainActivity : AppCompatActivity(), ContractInterface.View {

    private lateinit var loadingConstraintLayout: ConstraintLayout
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var usernameAdapter : UsernameAdapter

    private var presenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this, this)
    }

    override fun init() {
        errorTextView = findViewById(R.id.error_text_view)
        mainRecyclerView = findViewById(R.id.username_recycler_view)
    }


    override fun initSearchComponent() {
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<Button>(R.id.search_button)
        loadingConstraintLayout = findViewById(R.id.loading_constraint_view)

        searchButton.setOnClickListener{
            presenter!!.handleOnClick(searchEditText)
        }
    }

    override fun initiateAdapter(usernames: MutableList<Item>) {
        usernameAdapter = UsernameAdapter(presenter!!)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)

        mainRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = usernameAdapter
        }

        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if(presenter!!.haveMoreData() && isLastPosition && !usernameAdapter.isLoading()) {
                    presenter!!.getNextPage()
                }
            }
        })
    }

    override fun processNoData() {
        errorTextView.text = getText(R.string.no_data_string)
        showErrorText()
    }

    override fun processErrorCall() {
        errorTextView.text = getText(R.string.error_string)
        showErrorText()
    }

    private fun showErrorText() {
        errorTextView.visibility = View.VISIBLE
        mainRecyclerView.visibility = View.GONE
        loadingConstraintLayout.visibility = View.GONE
    }

    override fun hideErrorTextView() {
        errorTextView.visibility = View.GONE
    }

    override fun notifyDataChange() {
        usernameAdapter.notifyDataSetChanged()
    }

    override fun dismissProgressBar() {
        loadingConstraintLayout.visibility = View.GONE
    }

    override fun showProgressBar() {
        loadingConstraintLayout.visibility = View.VISIBLE
    }

    override fun addPagingProgressBar() {
        usernameAdapter.setIsLoading(true)
        presenter!!.addLoading()
        mainRecyclerView.post {
            usernameAdapter.notifyDataSetChanged()
        }
    }

    override fun removePagingProgressBar() {
        usernameAdapter.setIsLoading(false)
        presenter!!.removeLoading()
    }

    override fun hideKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun showToast(message: String) {
        Toast.makeText(this@MainActivity,message,Toast.LENGTH_SHORT)
    }

    override fun showMainRecyclerView() {
        mainRecyclerView.visibility = View.VISIBLE
    }
}
