package com.consumer

import com.core.HelloCore
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    val context = SpringApplicationBuilder(ConsumerApplication::class.java).run(*args)
    val exit = SpringApplication.exit(context)

    val core = HelloCore()
    core.hello()
    HelloCore.Companion.hello()

    runApplication<ConsumerApplication>(*args)
    exitProcess(exit)
}
