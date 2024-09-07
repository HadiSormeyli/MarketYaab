package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.google.gson.annotations.SerializedName
import com.hadisormeyli.marketyab.domain.models.location.Step

data class StepDto(
    @SerializedName("bearing_after")
    val bearingAfter: Int,
    val distance: DistanceDto,
    val duration: DurationDto,
    val exit: Int,
    val instruction: String,
    val modifier: String,
    val name: String,
    val polyline: String,
    val rotaryName: String,
    @SerializedName("start_location")
    val startLocation: List<Double>,
    val type: String
)

fun StepDto.toStep() : Step = Step(
    bearingAfter = bearingAfter,
    distance = distance.toDistance(),
    duration = duration.toDuration(),
    exit = exit,
    instruction = instruction,
    modifier = modifier,
    name = name,
    polyline = polyline,
    rotaryName = rotaryName,
    startLocation = startLocation,
    type = type
)
