package com.ms.multi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DefaultBatchApplication

fun main(args: Array<String>) {
    val core = HelloCore()
    core.hello()
    HelloCore.hello()

    runApplication<DefaultBatchApplication>(*args)
}
