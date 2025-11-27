package com.ms.multi.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class LoggingJobExecutionListener : JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
        println("""
            ╔════════════════════════════════════════╗
            ║  Job 시작: ${jobExecution.jobInstance.jobName}
            ║  Job ID: ${jobExecution.jobId}
            ║  시작 시간: ${jobExecution.startTime}
            ╚════════════════════════════════════════╝
        """.trimIndent())
    }

    override fun afterJob(jobExecution: JobExecution) {
        val duration = if (jobExecution.endTime != null && jobExecution.startTime != null) {
            10
        } else {
            0
        }

        println("""
            ╔════════════════════════════════════════╗
            ║  Job 완료: ${jobExecution.jobInstance.jobName}
            ║  상태: ${jobExecution.status}
            ║  종료 시간: ${jobExecution.endTime}
            ║  실행 시간: ${duration}ms
            ╚════════════════════════════════════════╝
        """.trimIndent())
    }
}
