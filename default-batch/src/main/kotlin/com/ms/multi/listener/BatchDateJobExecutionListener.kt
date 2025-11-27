package com.ms.multi.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BatchDateJobExecutionListener : JobExecutionListener {
    override fun beforeJob(jobExecution: JobExecution) {
        val batchDate = LocalDate.now()
        jobExecution.executionContext.putString("batchDate", batchDate.toString())
        println("배치 기준일: $batchDate")
    }
}
