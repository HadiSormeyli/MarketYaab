package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.hadisormeyli.marketyab.domain.models.product.Order

data class OrderDto(
    val order_id: Int,
    val minutes_left: Int,
    val order_date: String,
    val secret_phrase: String,
    val status: String,
    val store_name: String,
    val total_purchase_price: Int
)

fun OrderDto.toOrder() = Order(
    orderId = order_id,
    minutesLeft = minutes_left,
    orderDate = order_date,
    secretPhrase = secret_phrase,
    status = status,
    storeName = store_name,
    totalPurchasePrice = total_purchase_price
)