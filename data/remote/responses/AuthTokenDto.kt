package com.hadisormeyli.marketyab.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.user.AuthToken
import com.hadisormeyli.marketyab.domain.models.user.UserRole

data class AuthTokenDto(
    @SerializedName("jwt")
    val token: String,
    @SerializedName("user_role")
    val userRole: String
) : BaseResponse()

fun AuthTokenDto.toAuthToken() = AuthToken(
    token = token,
    userRole = if (userRole == "seller") UserRole.STORE else UserRole.CUSTOMER
)