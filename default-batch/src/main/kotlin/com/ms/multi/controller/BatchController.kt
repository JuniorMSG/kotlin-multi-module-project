package com.ms.multi.controller

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/batch")
class BatchController(
    private val jobLauncher: JobLauncher,
    private val simpleJob: Job
) {

    @GetMapping("/run")
    fun runBatch(): String {
        val jobParameters = JobParametersBuilder()
            .addString("requestTime", LocalDateTime.now().toString())
            .toJobParameters()

        val jobExecution = jobLauncher.run(simpleJob, jobParameters)

        return """
            배치 실행 완료!
            - Job Name: ${jobExecution.jobInstance.jobName}
            - Status: ${jobExecution.status}
            - Start Time: ${jobExecution.startTime}
            - End Time: ${jobExecution.endTime}
        """.trimIndent()
    }
}
