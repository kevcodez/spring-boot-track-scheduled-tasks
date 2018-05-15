package com.example.demo.tracking

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.time.Instant
import java.util.*

data class ScheduledJobRun(
    val uuid: UUID,
    val startedAt: Instant,
    val endedAt: Instant? = null,
    val exception: Throwable? = null
) {

    @JsonProperty("duration")
    fun duration(): Duration? {
        if (endedAt == null)
            return null

        return Duration.between(startedAt, endedAt)
    }
}
