package com.ms.multi.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener

abstract class SampleListener : JobExecutionListener {
    abstract fun supports(jobName: String): Boolean

    abstract fun recoverFailedState(jobExecution: JobExecution)

    override fun afterJob(jobExecution: JobExecution) {
        println("")
    }


}
