package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyaab.data.remote.responses.products.CommentDto

data class CommentResponse(
    @SerializedName("group_number")
    val page: Int,
    @SerializedName("group_size")
    val pageSize: Int,
    val comments: List<CommentDto>,
)