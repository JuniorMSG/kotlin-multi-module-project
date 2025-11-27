package com.ms.multi.sample.step

import org.springframework.batch.item.ItemReader
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Component("sampleReader")
@ConditionalOnProperty(
    prefix = "spring.batch.job",
    name = ["name"],
    havingValue = "sampleUpdateJob",
)
class SampleReader : ItemReader<SampleData> {
    private val data =
        mutableListOf(
            SampleData(1L, "Item 1"),
            SampleData(2L, "Item 2"),
            SampleData(3L, "Item 3"),
            SampleData(4L, "Item 4"),
            SampleData(5L, "Item 5"),
        )

    override fun read(): SampleData? {
        return if (data.isNotEmpty()) {
            val item = data.removeAt(0)
            println("📖 Reading: $item")
            item
        } else {
            null // 더 이상 읽을 데이터 없음
        }
    }
}
