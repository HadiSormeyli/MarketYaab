package com.hadisormeyli.marketyab.data.remote.responses

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("server_message")
    val serverMessage: String? = null
}