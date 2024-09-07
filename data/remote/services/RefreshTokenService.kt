package com.hadisormeyli.marketyaab.data.remote.services

import retrofit2.Response
import retrofit2.http.GET

interface RefreshTokenService {

    @GET
    suspend fun refreshAccessToken(refreshToken: String): Response<String>
}