package com.ms.multi.controller

import com.ms.multi.producer.OrderProducer
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderProducer: OrderProducer
) {

    @PostMapping
    fun createOrder(@RequestBody request: OrderRequest): OrderResponse {
        // Kafka로 메시지 발행
        orderProducer.sendOrder(request)

        return OrderResponse(
            success = true,
            message = "주문이 Kafka로 전송되었습니다!",
            orderId = request.orderId
        )
    }
}

// 요청 DTO
data class OrderRequest(
    val orderId: String,
    val productName: String,
    val quantity: Int,
    val price: Int
)

// 응답 DTO
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val orderId: String
)
