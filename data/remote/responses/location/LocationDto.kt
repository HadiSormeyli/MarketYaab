package com.hadisormeyli.marketyaab.data.remote.responses.location

import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import com.hadisormeyli.marketyab.domain.models.location.Location

data class LocationDto(
    val id: Int,
    val title: String,
    val latitude: Double,
    val longitude: Double,
) : BaseResponse()

fun LocationDto.toLocation() = Location(
    id = id,
    title = title,
    latitude = latitude,
    longitude = longitude,
)