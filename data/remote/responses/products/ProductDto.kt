package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.product.Product

class ProductDto(
    @SerializedName("product_id")
    val id: Int,
    val title: String,
    val unit: String,
    @SerializedName("price_per_unit")
    val price: Int,
    val distance: String,
    @SerializedName("picture_id")
    val imageUrl: String,
    @SerializedName("expire_time")
    val expirationTime: Long
)

fun ProductDto.toProduct() = Product(
    id = id,
    title = title,
    price = price,
    amount = "",
    distance = distance,
    imageUrl = imageUrl,
    expirationTime = expirationTime,
    unit = unit,
    isLiked = false
)

