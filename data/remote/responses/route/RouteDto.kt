package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.location.Route

data class RouteDto(
    val legs: List<LegDto>,
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolylineDto
)

fun RouteDto.toRoute() = Route(
    legs = legs.map { it.toLeg() },
    overviewPolyline = overviewPolyline.toOverviewPolyline()
)