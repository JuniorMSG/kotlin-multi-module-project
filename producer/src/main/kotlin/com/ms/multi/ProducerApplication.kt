package com.ms.multi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class ProducerApplication

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}
