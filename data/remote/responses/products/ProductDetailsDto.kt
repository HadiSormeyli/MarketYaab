package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.product.ProductDetails
import com.hadisormeyli.marketyab.domain.models.store.Store
import com.hadisormeyli.marketyab.domain.models.store.SubCategory
import com.hadisormeyli.marketyab.domain.models.store.Unit

data class ProductDetailsDto(
    @SerializedName("product_id")
    val id: Int,
    val title: String,
    val category: CategoryDto,
    @SerializedName("sub_category")
    val subCategory: SubCategory? = null,
    val description: String,
    @SerializedName("picture_id")
    val imageUrl: String,
    @SerializedName("price_per_unit")
    val price: Int,
    val unit: Unit,
    val distance: String? = null,
    val store: Store,
    @SerializedName("amount_in_cart")
    val amount: Int,
    @SerializedName("expire_time_epoch")
    val expireTime: Long,
    @SerializedName("product_score")
    val score: Double,
    val available_amount: Int,
    val comments: List<CommentDto>,
    val properties: Properties,
) : BaseResponse()

fun ProductDetailsDto.toProductDetails() = ProductDetails(
    id = id,
    title = title,
    category = category.toCategory(),
    subCategory = subCategory,
    description = description,
    imageUrl = imageUrl,
    price = price,
    unit = unit,
    distance = distance ?: "",
    availableAmount = available_amount,
    store = store,
    score = score,
    comments = comments.map { it.toComment() },
    properties = properties,
    amount = amount,
    isLiked = false,
    expireDate = expireTime
)
