package com.ms.multi.listener

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class SampleExecutionListener(
) : JobExecutionListener {
   override fun beforeJob(jobExecution: JobExecution) {
       println("Before JobExecutionListener :: ")
   }

    companion object {
        private const val SETTLEMENT_DATE_KEY = "samplxced"
    }
}
