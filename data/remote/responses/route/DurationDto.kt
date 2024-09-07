package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.hadisormeyli.marketyab.domain.models.location.Duration

data class DurationDto(
    val text: String,
    val value: Int
)

fun DurationDto.toDuration(): Duration = Duration(text, value)
