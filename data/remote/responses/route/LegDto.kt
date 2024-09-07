package com.hadisormeyli.marketyaab.data.remote.responses.route

import com.hadisormeyli.marketyab.domain.models.location.Leg

data class LegDto(
    val distance: DistanceDto,
    val duration: DurationDto,
    val steps: List<StepDto>,
    val summary: String
)

fun LegDto.toLeg() : Leg = Leg(
    distance = distance.toDistance(),
    duration = duration.toDuration(),
    steps = steps.map { it.toStep() },
    summary = summary
)