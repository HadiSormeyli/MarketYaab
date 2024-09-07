package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.product.Comment

data class CommentDto(
    @SerializedName("comment_id")
    val id: Int,
    val writer: String,
    val title: String,
    val description: String,
    @SerializedName("submission_time_epoch")
    val time: Long,
    @SerializedName("user_score")
    val score: Double,
    val product: CommentProduct? = null
) : BaseResponse()

fun CommentDto.toComment() = Comment(
    id = id,
    writer = writer,
    title = title,
    description = description,
    time = time,
    score = score,
    productId = product?.id ?: -1,
    productName = product?.title ?: ""
)