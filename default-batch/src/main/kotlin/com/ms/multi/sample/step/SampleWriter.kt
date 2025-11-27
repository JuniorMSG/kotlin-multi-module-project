package com.ms.multi.sample.step

import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component("sampleWriter")
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = "sampleUpdateJob",
)
class SampleWriter : ItemWriter<SampleData> {
    override fun write(chunk: Chunk<out SampleData>) {
        println("✍️ Writing ${chunk.size()} items:")

        chunk.items
            .filterNotNull()
            .forEach { item ->
                println("  ✅ Saved: $item")
                // 실제로는 여기서 DB 저장 등을 수행
                // repository.save(item)
            }
    }
}
