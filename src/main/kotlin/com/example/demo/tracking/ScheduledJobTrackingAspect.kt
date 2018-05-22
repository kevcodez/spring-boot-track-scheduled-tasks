package com.example.demo.tracking

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Aspect
@Configuration
class ScheduledJobTrackingAspect @Autowired constructor(
    private val scheduledJobTracker: ScheduledJobTracker
) {

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    fun around(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature as MethodSignature

        val uuid = scheduledJobTracker.jobStart(signature)
        var exception: Throwable? = null

        try {
            joinPoint.proceed()
        } catch (t: Throwable) {
            exception = t
        }

        scheduledJobTracker.jobEnd(uuid, signature, exception)
        if (exception != null)
            throw exception
    }

}
