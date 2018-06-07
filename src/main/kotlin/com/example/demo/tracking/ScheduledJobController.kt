package com.example.demo.tracking

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("\${tracking.scheduledJobs.path:/scheduled-jobs}")
@CrossOrigin(origins = ["*"])
class ScheduledJobController @Autowired constructor(
    private val scheduledJobTracker: ScheduledJobTracker
) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getScheduledJobs(): ResponseEntity<Any> {
        val scheduledJobs = scheduledJobTracker.trackedJobs
        return ResponseEntity.ok(scheduledJobs)
    }

    @RequestMapping(value = ["{className}"], method = [RequestMethod.GET])
    fun getScheduledJobsPerClass(@PathVariable(name = "className") className: String): ResponseEntity<Any> {
        val scheduledJobs = scheduledJobTracker.findJobsByClass(className)
        return ResponseEntity.ok(scheduledJobs)
    }

    @RequestMapping(value = ["{className}/{methodName}"], method = [RequestMethod.GET])
    fun getScheduledJob(
        @PathVariable(name = "className") className: String,
        @PathVariable(name = "methodName") methodName: String
    ): ResponseEntity<Any> {
        val scheduledJob = scheduledJobTracker.findJobByClassAndMethod(className, methodName)
        if (scheduledJob == null)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(scheduledJob)
    }

    @RequestMapping(value = ["{className}/{methodName}/{uuid}"], method = [RequestMethod.GET])
    fun getScheduledJobRun(
        @PathVariable(name = "className") className: String,
        @PathVariable(name = "methodName") methodName: String,
        @PathVariable(name = "uuid") uuid: String
    ): ResponseEntity<Any> {
        val scheduledJobRun = scheduledJobTracker.findRunByUuid(className, methodName, uuid)
        if (scheduledJobRun == null)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(scheduledJobRun)
    }

    @RequestMapping(
        value = ["{className}/{methodName}"],
        method = [RequestMethod.POST]
    )
    open fun triggerJob(
        @PathVariable(name = "className") className: String,
        @PathVariable(name = "methodName") methodName: String
    ): ResponseEntity<Any> {
        val successful = scheduledJobTracker.triggerJob(className, methodName)

        if (successful)
            return ResponseEntity(HttpStatus.OK)
        else
            return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

}