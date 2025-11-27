package com.ms.multi.sample.step

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component("sampleListener")
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = "sampleUpdateJob",
)
class SampleListener : StepExecutionListener {

    override fun beforeStep(stepExecution: StepExecution) {
        println("🚀 Step 시작: ${stepExecution.stepName}")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        println("🏁 Step 종료: ${stepExecution.stepName}")
        println("   - Read Count: ${stepExecution.readCount}")
        println("   - Write Count: ${stepExecution.writeCount}")
        println("   - Status: ${stepExecution.status}")

        return stepExecution.exitStatus
    }
}
