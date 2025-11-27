package com.ms.multi.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class SimpleJobConfig {

    /**
     * Job 정의
     */
    @Bean
    fun simpleJob(
        jobRepository: JobRepository,
        simpleStep: Step
    ): Job {
        return JobBuilder("simpleJob", jobRepository)
            .start(simpleStep)
            .build()
    }

    /**
     * Step 정의
     */
    @Bean
    fun simpleStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Step =
        StepBuilder("simpleStep", jobRepository)
            .tasklet(
                { _, _ ->
                    println("======================")
                    println("배치 작업 실행!")
                    println("현재 시간: ${java.time.LocalDateTime.now()}")
                    println("======================")
                    RepeatStatus.FINISHED
                },
                transactionManager,
        ).build()
}
