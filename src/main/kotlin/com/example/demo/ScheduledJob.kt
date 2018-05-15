package com.example.demo

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledJob {

    @Scheduled(fixedRate = 5000)
    fun println() = println("foo")

}