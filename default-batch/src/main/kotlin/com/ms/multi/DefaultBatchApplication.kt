package com.ms.multi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import kotlin.system.exitProcess

@SpringBootApplication
class DefaultBatchApplication

fun main(args: Array<String>) {
    val core = HelloCore()
    core.hello()
    HelloCore.hello()

    val context = SpringApplicationBuilder(DefaultBatchApplication::class.java).run(*args)
    val exit = SpringApplication.exit(context)
    exitProcess(exit)
}
