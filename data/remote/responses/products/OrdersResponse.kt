package com.hadisormeyli.marketyaab.data.remote.responses.products

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.product.Orders

data class OrdersResponse(
    @SerializedName("possible_order_status")
    val status: List<String>,
    val orders: List<OrderDto>
)

fun OrdersResponse.toOrders() = Orders(
    status = status,
    orders = orders.map { it.toOrder() }
)