package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.hadisormeyli.marketyab.domain.models.location.OverviewPolyline

data class OverviewPolylineDto(
    val points: String
)

fun OverviewPolylineDto.toOverviewPolyline(): OverviewPolyline = OverviewPolyline(points)