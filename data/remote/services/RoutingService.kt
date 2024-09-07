package com.hadisormeyli.marketyaab.data.remote.services

import com.hadisormeyli.marketyaab.data.remote.responses.route.RoutesDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RoutingService {
    @GET("v4/direction")
    suspend fun getRoutes(
        @Header("Api-Key") apiKey: String = "service.45c57ccaa68549e7a777110b87b9d189",
        @Query("type") type: String = "car",
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("avoidTrafficZone") avoidTrafficZone: Boolean = false,
        @Query("avoidOddEvenZone") avoidOddEvenZone: Boolean = false,
        @Query("alternative") alternative: Boolean = false,
        @Query("bearing") bearing: String = ""
    ): RoutesDto
}