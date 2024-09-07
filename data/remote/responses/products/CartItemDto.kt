package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.hadisormeyli.marketyab.domain.models.product.CartItem
import com.hadisormeyli.marketyab.domain.models.store.Store

data class CartItemDto(
    val product_id: Int,
    val title: String,
    val cart_item_id: Int,
    val picture_id: String,
    val cart_item_amount: Int,
    val unit: String,
    val price_per_unit: Int,
    val store: Store,
    val available_amount:Int
)

fun CartItemDto.toCartItem() = CartItem(
    id = product_id,
    title = title,
    cartItemId = cart_item_id,
    imageUrl = picture_id,
    amount = cart_item_amount,
    unit = unit,
    price = price_per_unit,
    store = store,
    availableAmount = available_amount,
)

