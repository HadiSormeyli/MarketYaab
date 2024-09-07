package com.hadisormeyli.marketyaab.data.remote.services

import com.hadisormeyli.marketyab.data.remote.responses.AuthTokenDto
import com.hadisormeyli.marketyab.data.remote.responses.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {

    @FormUrlEncoded
    @POST("/v1/login")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): AuthTokenDto

    @FormUrlEncoded
    @POST("/v1/check_phone")
    suspend fun checkPhoneNumber(@Field("phone") phone: String): BaseResponse

    @Multipart
    @POST("/v1/sign_up_buyer")
    suspend fun registerCustomer(
        @Part("phone") phone: String,
        @Part("password") password: String,
        @Part("first_name") firstname: String,
        @Part("last_name") lastname: String,
        @Part("latitude") latitude: Double,
        @Part("longitude") longitude: Double,
    ): AuthTokenDto

    @Multipart
    @POST("/v1/store_registration")
    suspend fun registerStore(
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("owner_first_name") firstname: RequestBody,
        @Part("owner_last_name") lastname: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null,
        @Part("store_name") storeName: RequestBody,
        @Part("store_latitude") latitude: Double,
        @Part("store_longitude") longitude: Double,
        @Part storeProfile: MultipartBody.Part?,
        @Part("working_times") workTimes: RequestBody,
    ): AuthTokenDto
}