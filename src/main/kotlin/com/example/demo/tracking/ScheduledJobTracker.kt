package com.example.demo.tracking

import org.aspectj.lang.reflect.MethodSignature
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import kotlin.collections.HashSet

@Component
class ScheduledJobTracker {

    var trackedJobs: MutableSet<TrackedScheduledJob> = HashSet()

    fun jobStart(signature: MethodSignature): UUID {
        val uuid = UUID.randomUUID()

        val scheduledJob = getOrAddJob(signature)
        scheduledJob.addRun(
            ScheduledJobRun(
                uuid = uuid,
                startedAt = Instant.now()
            )
        )

        return uuid
    }

    private fun getOrAddJob(signature: MethodSignature): TrackedScheduledJob {
        val className = signature.declaringTypeName
        val methodName = signature.method.name

        var trackedJob = trackedJobs.find { it.className == className && it.methodName == methodName }

        if (trackedJob == null) {
            trackedJob = addJob(signature)
            trackedJobs.add(trackedJob)
        }

        return trackedJob
    }

    private fun addJob(signature: MethodSignature): TrackedScheduledJob {
        val annotation = signature.method.getAnnotation(
            Scheduled::class.java
        )

        return TrackedScheduledJob(
            className = signature.declaringTypeName,
            methodName = signature.method.name,
            cron = nullIfEmptyString(annotation.cron),
            fixedRate = nullIfNegativeNumber(annotation.fixedRate),
            fixedRateString = nullIfEmptyString(annotation.fixedRateString),
            initialDelay = nullIfNegativeNumber(annotation.initialDelay),
            initialDelayString = nullIfEmptyString(annotation.initialDelayString),
            fixedDelay = nullIfNegativeNumber(annotation.fixedDelay),
            fixedDelayString = nullIfEmptyString(annotation.fixedDelayString)
        )
    }

    private fun nullIfEmptyString(str: String) : String? {
        return if (str.isEmpty()) null else str
    }

    private fun nullIfNegativeNumber(number: Long) : Long? {
        return if (number < 0) null else number
    }

    fun jobEnd(uuid: UUID, signature: MethodSignature, exception: Throwable?) {
        val className = signature.declaringTypeName
        val methodName = signature.method.name
        val trackedJob = trackedJobs.find { it.className == className && it.methodName == methodName }!!
        trackedJob.endRun(uuid, exception)
    }

}

