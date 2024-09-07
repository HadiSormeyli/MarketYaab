package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyaab.data.remote.responses.products.ProductDto
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse

data class ProductResponse(
    @SerializedName("group_number")
    val page: Int,
    @SerializedName("products_per_group")
    val pageSize: Int,
    @SerializedName("total_groups")
    val total: Int,
    val products: List<ProductDto>
) : BaseResponse()