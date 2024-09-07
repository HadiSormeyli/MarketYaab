package com.hadisormeyli.marketyaab.data.remote.requests

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.store.ShiftTime

data class WorkTimesRequest(
    @SerializedName("day_sequence_id")
    val day: Int,
    @SerializedName("is_holiday_binary")
    val isHoliday: Int,
    @SerializedName("times")
    val shiftTimes: ShiftTime
)