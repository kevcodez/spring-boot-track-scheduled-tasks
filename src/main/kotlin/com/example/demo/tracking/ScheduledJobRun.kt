package com.example.demo.tracking

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Duration
import java.time.Instant
import java.util.*

@JsonInclude(value = JsonInclude.Include.NON_NULL)
data class ScheduledJobRun(
    val uuid: UUID,
    val startedAt: Instant,
    val endedAt: Instant? = null,
    val exception: Throwable? = null
) {

    fun durationInMs(): Long? {
        if (endedAt == null)
            return null

        return Duration.between(startedAt, endedAt).toMillis()
    }
}
