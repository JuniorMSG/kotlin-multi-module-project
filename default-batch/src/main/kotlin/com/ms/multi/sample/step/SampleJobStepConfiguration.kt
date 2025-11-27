package com.ms.multi.sample.step

import org.springframework.batch.core.Step
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = "sampleUpdateJob", // Job 이름과 일치
)
class SampleUpdateJobStepConfiguration {

    @Bean(STEP_NAME)
    fun sampleStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        @Qualifier("sampleReader") reader: ItemReader<SampleData>,
        @Qualifier("sampleProcessor") processor: ItemProcessor<SampleData, SampleData>,
        @Qualifier("sampleWriter") writer: ItemWriter<SampleData>,
    ): Step =
        StepBuilder(STEP_NAME, jobRepository)
            .chunk<SampleData, SampleData>(10, transactionManager) // 청크 사이즈 10
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()

    companion object {
        const val STEP_NAME = "sampleUpdateStep"
    }
}

// 간단한 데이터 클래스
data class SampleData(
    val id: Long,
    val name: String,
    val processed: Boolean = false
)
