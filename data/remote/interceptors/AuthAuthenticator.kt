package com.hadisormeyli.marketyaab.data.remote.interceptors

import com.hadisormeyli.marketyaab.data.local.db.dao.AuthTokenDao
import com.hadisormeyli.marketyab.ui.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val sessionManager: SessionManager,
    private val authTokenDao: AuthTokenDao
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = authTokenDao.getToken() ?: return@runBlocking null
//            val newToken = refreshAccessToken(refreshToken) ?: return@runBlocking null

            response.request.newBuilder()
                .header("Authorization", "Bearer $refreshToken")
                .build()
        }
    }

//    override fun authenticate(route: Route?, response: Response): Request? {
//        return runBlocking {
//            val refreshToken = authTokenDao.getToken() ?: return@runBlocking null
//            val newToken = refreshAccessToken(refreshToken) ?: return@runBlocking null
//
//            response.request.newBuilder()
//                .header("Authorization", "Bearer $newToken")
//                .build()
//        }
//    }
//
//    private suspend fun refreshAccessToken(refreshToken: String): String? =
//        withContext(Dispatchers.IO) {
//            try {
//                val response = refreshTokenService.refreshAccessToken(refreshToken)
//                if (response.isSuccessful && response.body() != null) {
//                    val newToken = response.body()!!
//                    authTokenDao.insert(
//                        AuthTokenEntity(
//                            token = newToken,
//                            userRole = 1
//                        )
//                    )
//                    newToken
//                } else {
//                    sessionManager.logout()
//                    null
//                }
//            } catch (e: Exception) {
//                sessionManager.logout()
//                null
//            }
//        }
}
