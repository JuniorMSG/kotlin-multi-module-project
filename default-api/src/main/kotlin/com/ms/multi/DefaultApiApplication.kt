package com.ms.multi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DefaultApiApplication

fun main(args: Array<String>) {
    val core = HelloCore()
    core.hello()
    HelloCore.hello()

    runApplication<DefaultApiApplication>(*args)
}
