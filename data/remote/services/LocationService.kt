package com.hadisormeyli.marketyaab.data.remote.services

import com.hadisormeyli.marketyaab.data.remote.responses.location.SearchLocationDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LocationService {

    @FormUrlEncoded
    @POST("/v1/place_recommender")
    suspend fun searchLocation(
        @Field("query") query: String,
    ): List<SearchLocationDto>
}