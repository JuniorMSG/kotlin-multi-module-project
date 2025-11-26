package com.ms.multi.producer

import com.ms.multi.controller.OrderRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class OrderProducer(
    private val kafkaTemplate: KafkaTemplate<String, OrderRequest>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendOrder(order: OrderRequest) {
        logger.info("📤 Kafka로 주문 전송: $order")

        kafkaTemplate.send("order-topic", order.orderId, order)

        logger.info("✅ 전송 완료!")
    }
}
