package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.product.Cart

data class CartDto(
    val products: List<CartItemDto>
) : BaseResponse()

fun CartDto.toCart() = Cart(
    serverMessage = serverMessage,
    products = products.map { it.toCartItem() }
)