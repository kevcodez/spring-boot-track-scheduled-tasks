package com.example.demo

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class ScheduledJob {

    @Scheduled(fixedRate = 5000)
    fun println() = println("foo")

    @Scheduled(fixedRate = 15000)
    fun regularJob() = println("Just a regular job")

    @Scheduled(fixedRate = 30000)
    fun longRunningJob() {
        Thread.sleep(10000)
    }

    @Scheduled(fixedRate = 10000)
    fun sometimesThrowingException() {
        val random = Random().nextInt(10)
        if (random <= 2)
            throw IllegalArgumentException("Random exception")
    }

    @Scheduled(fixedRate = 60000)
    fun alwaysException() {
        throw IllegalArgumentException("Computer says no")
    }

    @Scheduled(initialDelay = 1000000000, fixedRate = 1000000000000000)
    fun neverRunningJob() = println("never")

}