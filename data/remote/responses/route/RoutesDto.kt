package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.hadisormeyli.marketyab.domain.models.location.Routes

data class RoutesDto(
    val routes: List<RouteDto>
)

fun RoutesDto.toRoutes() = Routes(
    routes = routes.map { it.toRoute() }
)