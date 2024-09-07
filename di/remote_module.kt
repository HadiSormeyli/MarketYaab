package com.hadisormeyli.marketyaab.di

import com.google.gson.Gson
import com.hadisormeyli.marketyaab.constant.Constants.AUTHENTICATED_OKHTTP_CLIENT
import com.hadisormeyli.marketyaab.constant.Constants.AUTHENTICATED_RETROFIT
import com.hadisormeyli.marketyaab.constant.Constants.CONNECT_TIMEOUT
import com.hadisormeyli.marketyaab.constant.Constants.NESHAN_RETROFIT
import com.hadisormeyli.marketyaab.constant.Constants.NESHAN_URL
import com.hadisormeyli.marketyaab.constant.Constants.PUBLIC_OKHTTP_CLIENT
import com.hadisormeyli.marketyaab.constant.Constants.PUBLIC_RETROFIT
import com.hadisormeyli.marketyaab.constant.Constants.READ_TIMEOUT
import com.hadisormeyli.marketyaab.constant.Constants.SERVER_URL
import com.hadisormeyli.marketyaab.constant.Constants.WRITE_TIMEOUT
import com.hadisormeyli.marketyaab.data.remote.interceptors.AccessTokenInterceptor
import com.hadisormeyli.marketyaab.data.remote.interceptors.RefreshTokenInterceptor
import com.hadisormeyli.marketyaab.data.remote.services.AuthService
import com.hadisormeyli.marketyaab.data.remote.services.CommonService
import com.hadisormeyli.marketyaab.data.remote.services.CustomerService
import com.hadisormeyli.marketyaab.data.remote.services.LocationService
import com.hadisormeyli.marketyaab.data.remote.services.RoutingService
import com.hadisormeyli.marketyaab.data.remote.services.StoreService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
    single { AccessTokenInterceptor(get(), get()) }
//    single { AuthAuthenticator(get(), get()) }

    single(named(PUBLIC_OKHTTP_CLIENT)) { createPublicOkHttpClient(get()) }
    single(named(AUTHENTICATED_OKHTTP_CLIENT)) {
        createAuthenticatedOkHttpClient(get(), get())
    }

    single(named(PUBLIC_RETROFIT)) {
        createRetrofit(
            get(named(PUBLIC_OKHTTP_CLIENT)),
            get()
        )
    }
    single(named(AUTHENTICATED_RETROFIT)) {
        createRetrofit(
            get(named(AUTHENTICATED_OKHTTP_CLIENT)),
            get()
        )
    }

    single { createWebService<LocationService>(get(named(PUBLIC_RETROFIT))) }
}

fun createRetrofit(
    okHttpClient: OkHttpClient,
    gsonBuilder: Gson,
    url: String = SERVER_URL
): Retrofit =
    Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
        .build()

fun createPublicOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

fun createAuthenticatedOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    accessTokenInterceptor: AccessTokenInterceptor,
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(accessTokenInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .build()
}

fun createRefreshOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    refreshTokenInterceptor: RefreshTokenInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(refreshTokenInterceptor)
        .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createWebService(retrofit: Retrofit): T = retrofit.create(T::class.java)
