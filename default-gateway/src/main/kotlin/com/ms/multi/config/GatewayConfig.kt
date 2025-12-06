package com.ms.multi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig {
    @Value("\${payment.service.uri:http://localhost:10001}")
    private lateinit var paymentServiceUri: String

    @Bean
    fun customRoutes(builder: RouteLocatorBuilder): RouteLocator {
        println("🔧 Configuring Gateway Routes...")
        println("📍 Payment Service URI: $paymentServiceUri")

        return builder.routes()
            .route("payment-service") { r ->
                r.path("/api/payment/**")
                    .filters { f ->
                        f.addRequestHeader("X-Gateway", "true")
                    }
                    .uri(paymentServiceUri)
            }
            .build()
            .also {
                println("✅ Gateway Routes configured")
            }
    }
}
