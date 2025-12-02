package com.ms.multi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DefaultGatewayApplication

fun main(args: Array<String>) {
    runApplication<DefaultGatewayApplication>(*args)
}
