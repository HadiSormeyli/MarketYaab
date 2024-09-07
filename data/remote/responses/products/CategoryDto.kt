package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.product.Category

data class CategoryDto(
    val id: Int,
    val name: String,
    @SerializedName("picture_id")
    val picture: String?
) : BaseResponse()

fun CategoryDto.toCategory() = Category(
    id = id,
    name = name,
    imageUrl = picture ?: ""
)