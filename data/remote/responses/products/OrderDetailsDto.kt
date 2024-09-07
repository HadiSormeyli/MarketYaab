package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.hadisormeyli.marketyab.domain.models.product.OrderDetails
import com.hadisormeyli.marketyab.domain.models.product.OrderProduct
import com.hadisormeyli.marketyab.domain.models.product.Seller
import com.hadisormeyli.marketyab.domain.models.store.Store

data class OrderDetailsDto(
    val order_id: Int,
    val minutes_left: Int,
    val seller: Seller,
    val secret_phrase: String,
    val order_date: String,
    val status: String,
    val total_purchase_price: Int,
    val store: Store,
    val products: List<OrderProduct>,
)

fun OrderDetailsDto.toOrderDetails() = OrderDetails(
    orderId = order_id,
    minutesLeft = minutes_left,
    orderDate = order_date,
    seller = seller,
    secretPhrase = secret_phrase,
    status = status,
    totalPurchasePrice = total_purchase_price,
    store = store,
    products = products
)