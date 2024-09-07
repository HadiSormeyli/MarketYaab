package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.hadisormeyli.marketyab.domain.models.location.Distance

data class DistanceDto(
    val text: String,
    val value: Int
)

fun DistanceDto.toDistance(): Distance = Distance(text, value)