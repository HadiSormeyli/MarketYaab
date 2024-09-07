package com.hadisormeyli.marketyab.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status_code")
    val statusCode: String,
): BaseResponse()