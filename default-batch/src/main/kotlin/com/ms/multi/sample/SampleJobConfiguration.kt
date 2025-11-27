package com.ms.multi.sample

import com.ms.multi.config.ClearRunIdIncrementer
import com.ms.multi.sample.step.SampleUpdateJobStepConfiguration
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = SampleJobConfiguration.JOB_NAME,
)
class SampleJobConfiguration {
    @Bean(JOB_NAME)
    fun job(
        jobRepository: JobRepository,
        loggingJobExecutionListener: JobExecutionListener,
        @Qualifier(SampleUpdateJobStepConfiguration.STEP_NAME)
        sampleStep: Step, // 변수명 수정
    ): Job =
        JobBuilder(JOB_NAME, jobRepository)
            .incrementer(ClearRunIdIncrementer())
            .listener(loggingJobExecutionListener)
            .start(sampleStep)
            .build()

    companion object {
        const val JOB_NAME = "sampleUpdateJob"
    }
}
