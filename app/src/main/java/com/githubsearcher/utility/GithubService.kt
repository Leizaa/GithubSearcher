package com.githubsearcher.utility

import com.githubsearcher.model.ResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/users")
    fun getUsernameList(
        @Query("q") query: String,
        @Query("per_page") dataCount: Int,
        @Query("page") page: Int
    ) : Call<ResultData>
}