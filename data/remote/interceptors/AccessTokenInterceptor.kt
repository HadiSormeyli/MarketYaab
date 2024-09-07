package com.hadisormeyli.marketyaab.data.remote.interceptors

import com.hadisormeyli.marketyaab.data.local.db.dao.AuthTokenDao
import com.hadisormeyli.marketyab.ui.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AccessTokenInterceptor(
    private val authTokenDao: AuthTokenDao,
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authTokenDao.getToken()

        if (token.isNullOrEmpty() || token.isBlank()) {
            sessionManager.logout()
        }

        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
        requestBuilder.addHeader("Content-Type", "application/json")

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            sessionManager.logout()
        }

        return response
    }
}