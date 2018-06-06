package com.example.demo.tracking

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
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

    @JsonProperty
    fun duration(): Long? {
        if (endedAt == null)
            return null

        return Duration.between(startedAt, endedAt).toMillis()
    }

}
