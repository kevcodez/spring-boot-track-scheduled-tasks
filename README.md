# General

This project demonstrates the tracking of scheduled jobs using Spring AOP.

The tracking is fairly simple. The `ScheduledJobTrackingAspect` invokes every method with an `Scheduled` annotation and writes the start and end of the method into the `ScheduledJobTracker`.
A UUID is assigned to the job upon start, since a single job can run multiple times at once.

The data is available via `ScheduledJobController`.

This is just a project demonstrating the tracking. I am considering to build a library that can be included in any spring boot project to enable tracking of scheduled tasks.

## Sample scheduled job

The demo has a very simple scheduled job, that prints *foo* every 5 seconds.

```kotlin
package com.example.demo

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledJob {

    @Scheduled(fixedRate = 5000)
    fun println() = println("foo")

}
```

## Access data via REST

`GET /scheduled-jobs/`

```json
[
  {
    "className": "com.example.demo.ScheduledJob",
    "methodName": "println",
    "fixedRate": 5000,
    "runs": [
      {
        "uuid": "c5ca5c73-f1f6-4ced-a911-de0e43a78d78",
        "startedAt": "2018-05-15T23:38:11.177Z",
        "endedAt": "2018-05-15T23:38:11.182Z",
        "exception": null,
        "duration": "PT0.005S"
      },
      {
        "uuid": "2dd54fc3-8753-41c1-91d2-a7ec33fda0e8",
        "startedAt": "2018-05-15T23:38:16.169Z",
        "endedAt": "2018-05-15T23:38:16.169Z",
        "exception": null,
        "duration": "PT0S"
      },
      {
        "uuid": "6be5d3ad-924e-4aa6-aef8-a4b837b3e67f",
        "startedAt": "2018-05-15T23:38:21.170Z",
        "endedAt": "2018-05-15T23:38:21.170Z",
        "exception": null,
        "duration": "PT0S"
      },
      {
        "uuid": "72dc2d85-d2f1-4875-9381-3d26f6c6512c",
        "startedAt": "2018-05-15T23:38:26.169Z",
        "endedAt": "2018-05-15T23:38:26.169Z",
        "exception": null,
        "duration": "PT0S"
      },
      {
        "uuid": "e4f635ee-625b-4618-84ee-0cb0c2aa210e",
        "startedAt": "2018-05-15T23:38:31.169Z",
        "endedAt": "2018-05-15T23:38:31.169Z",
        "exception": null,
        "duration": "PT0S"
      }
    ],
    "status": "Idle",
    "averageDurationInMs": 1,
    "lastDurationInMs": 0,
    "lastRunStarted": "2018-05-15T23:38:31.169Z",
    "lastRunEnded": "2018-05-15T23:38:31.169Z"
  }
]
```