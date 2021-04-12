package com.githubsearcher.model

import com.google.gson.annotations.SerializedName

data class ResultData(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<Item>,
)

data class Item(
    @SerializedName("login") val loginName: String,
    @SerializedName("avatar_url") val avatarUrl: String
)
