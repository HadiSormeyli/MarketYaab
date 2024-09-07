package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.product.CategoryProducts

data class CategoryResponse(
    @SerializedName("group_number")
    val page: Int,
    @SerializedName("products_per_group")
    val pageSize: Int,
    @SerializedName("total_groups")
    val total: Int,
    val products: List<ProductDto>,
    val category: CategoryDto
) : BaseResponse()

fun CategoryResponse.toCategoryProducts() = CategoryProducts(
    products = products.map { it.toProduct() },
    category = category.toCategory()
)