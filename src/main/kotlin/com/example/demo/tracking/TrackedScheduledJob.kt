package com.example.demo.tracking

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TrackedScheduledJob(
    val className: String,
    val methodName: String,
    val cron: String?,
    val fixedRate: Long?,
    val fixedRateString: String?,
    val initialDelay: Long?,
    val initialDelayString: String?,
    val fixedDelay: Long?,
    val fixedDelayString: String?,
    val runs: MutableList<ScheduledJobRun> = ArrayList()
) {

    private val trackingLimit = 10

    fun addRun(run: ScheduledJobRun) {
        if (runs.size == trackingLimit)
            runs.removeAt(0)
        runs.add(run)
    }

    fun endRun(uuid: UUID, exception: Throwable?) {
        val endedAt = Instant.now()
        val run = runs.find { it.uuid == uuid } ?: return

        runs.remove(run)
        runs.add(run.copy(endedAt = endedAt, exception = exception))
    }

    @JsonProperty("lastRunStarted")
    fun lastRunStarted(): Instant? {
        return runs.maxBy { it.startedAt }?.startedAt
    }

    @JsonProperty("lastRunEnded")
    fun lastRunEnded(): Instant? {
        val endedRuns = runs.filter { it.endedAt != null }
        return endedRuns.maxBy { it.endedAt!! }?.endedAt
    }

    @JsonProperty("lastDurationInMs")
    fun lastDurationInMs(): Long? {
        val lastEndedRun = runs.filter { it.endedAt != null }.maxBy { it.endedAt!! }

        return lastEndedRun?.duration()?.toMillis()
    }

    @JsonProperty("averageDurationInMs")
    fun averageDurationInMs(): Double? {
        val finishedRuns = runs.filter { it.endedAt != null }

        if (finishedRuns.isEmpty())
            return null

        return finishedRuns.map { it.duration()!!.toMillis() }.average()
    }

    @JsonProperty("status")
    fun status(): String {
        val runningCount = runs.count { it.endedAt == null }
        return if (runningCount == 0) "Idle" else "Running"
    }

}
