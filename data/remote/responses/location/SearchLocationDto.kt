package com.hadisormeyli.marketyaab.data.remote.responses.location

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.location.SearchLocation

data class SearchLocationDto(
    @SerializedName("boundingbox")
    val boundingBox: List<Double>,
    val category: String,
    @SerializedName("display_name")
    val displayName: String,
    val importance: Double,
    val lat: Double,
    val licence: String,
    val lon: Double,
    @SerializedName("osm_id")
    val osmId: String,
    @SerializedName("osm_type")
    val osmType: String,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("place_rank")
    val placeRank: Int,
    val type: String
)

fun SearchLocationDto.toSearchLocation() = SearchLocation(
    id = placeId,
    address = displayName,
    latitude = lat,
    longitude = lon
)


