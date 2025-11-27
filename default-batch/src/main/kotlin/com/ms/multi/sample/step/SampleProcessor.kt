package com.ms.multi.sample.step

import org.springframework.batch.item.ItemProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component("sampleProcessor")
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = "sampleUpdateJob",
)
class SampleProcessor : ItemProcessor<SampleData, SampleData> {
    override fun process(item: SampleData): SampleData {
        println("⚙️ Processing: ${item.name}")

        // 간단한 처리: processed 플래그를 true로 변경
        return item.copy(
            name = "${item.name} (Processed)",
            processed = true,
        )
    }
}
