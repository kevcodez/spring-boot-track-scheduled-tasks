package com.example.demo.tracking

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ScheduledJobController @Autowired constructor(
    private val scheduledJobTracker: ScheduledJobTracker
) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getScheduledJobs(): ResponseEntity<Any> {
        val scheduledJobs = scheduledJobTracker.trackedJobs
        return ResponseEntity.ok(scheduledJobTracker.trackedJobs)
    }

    @RequestMapping(value = ["{className}"], method = [RequestMethod.GET])
    fun getScheduledJobsPerClass(@PathVariable(name = "className") className: String): ResponseEntity<Any> {
        val scheduledJobs = scheduledJobTracker.trackedJobs.filter { it.className == className }
        return ResponseEntity.ok(scheduledJobs)
    }

    @RequestMapping(value = ["{className}/{methodName}"], method = [RequestMethod.GET])
    fun getScheduledJobsPerMethod(@PathVariable(name = "className") className: String, @PathVariable(name = "methodName") methodName: String): ResponseEntity<Any> {
        val scheduledJobs =
            scheduledJobTracker.trackedJobs.filter { it.className == className && it.methodName == methodName }
        return ResponseEntity.ok(scheduledJobs)
    }

}