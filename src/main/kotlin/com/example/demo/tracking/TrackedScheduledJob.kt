package com.example.demo.tracking

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.time.Instant
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TrackedScheduledJob(
    val className: String,
    val methodName: String,
    val settings: Settings,
    val stats: Stats = Stats(),
    val runs: MutableList<ScheduledJobRun> = ArrayList()
) {

    private val trackingLimit = 10

    fun addRun(run: ScheduledJobRun) {
        stats.numberOfInvocations++
        if (runs.size == trackingLimit)
            runs.removeAt(trackingLimit - 1)
        runs.add(0, run)
    }

    fun endRun(uuid: UUID, exception: Throwable?) {
        val endedAt = Instant.now()
        val run = runs.find { it.uuid == uuid } ?: return

        val index = runs.indexOf(run)
        runs.remove(run)
        runs.add(index, run.copy(endedAt = endedAt, exception = exception))

        if (exception != null)
            stats.numberOfExceptions++

        val durationInMillis = Duration.between(run.startedAt, endedAt).toMillis()
        stats.totalTimeInMs += durationInMillis
        if (stats.shortestRunDurationInMs == null || durationInMillis < stats.shortestRunDurationInMs!!)
            stats.shortestRunDurationInMs = durationInMillis
        if (stats.longestRunDurationInMs == null || durationInMillis > stats.longestRunDurationInMs!!)
            stats.longestRunDurationInMs = durationInMillis
    }

    @JsonProperty
    fun lastFinishedRun(): ScheduledJobRun? {
        return runs.filter { it.endedAt != null }.sortedByDescending { it.startedAt }.firstOrNull()
    }

    @JsonProperty
    fun latestRun(): ScheduledJobRun? {
        return runs.sortedByDescending { it.startedAt }.firstOrNull()
    }

    @JsonProperty("currentlyRunning")
    fun currentlyRunning(): Boolean {
        return runs.any { it.endedAt == null }
    }

}

data class Settings(
    val cron: String?,
    val fixedRate: Long?,
    val fixedRateString: String?,
    val initialDelay: Long?,
    val initialDelayString: String?,
    val fixedDelay: Long?,
    val fixedDelayString: String?
)

data class Stats(
    var numberOfExceptions: Long = 0,
    var numberOfInvocations: Long = 0,
    var longestRunDurationInMs: Long? = null,
    var shortestRunDurationInMs: Long? = null,
    var totalTimeInMs: Long = 0
) {

    @JsonProperty
    fun averageDurationInMs(): Long? {
        if (numberOfInvocations == 0L)
            return null
        return totalTimeInMs / numberOfInvocations
    }
}
