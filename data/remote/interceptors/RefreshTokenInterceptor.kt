package com.hadisormeyli.marketyaab.data.remote.interceptors

import com.hadisormeyli.marketyaab.data.local.db.dao.AuthTokenDao
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RefreshTokenInterceptor(private val authTokenDao: AuthTokenDao) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authTokenDao.getAuthToken() }
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
            .build()
        requestBuilder.addHeader("Content-Type", "application/json").build()
        return chain.proceed(requestBuilder.build())
    }
}