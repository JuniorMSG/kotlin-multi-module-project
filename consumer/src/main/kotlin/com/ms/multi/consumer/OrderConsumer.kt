package com.ms.multi.consumer

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class OrderConsumer {

    private val logger = LoggerFactory.getLogger(javaClass)

    // 🔥 클래스 레벨로 이동! (메시지별로 카운트 관리)
    private val attemptCountMap = ConcurrentHashMap<String, Int>()

    @KafkaListener(
        topics = ["order-topic"],
        groupId = "order-consumer-group",
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    fun consumeOrder(order: OrderMessage) {
        logger.info("📨 Kafka에서 주문 수신: $order")

        // 현재 시도 횟수 증가
        val attemptCount = attemptCountMap.compute(order.orderId) { _, count ->
            (count ?: 0) + 1
        }!!

        logger.info("🔄 시도 횟수: $attemptCount (주문ID: ${order.orderId})")

        try {
            if (attemptCount < 3) {
                throw RuntimeException("일부러 실패! (시도: $attemptCount/3)")
            }

            // 3번째 시도에서 성공
            processOrder(order)
            logger.info("✅ 처리 완료 (3번째 시도에서 성공!)")

            // 성공하면 카운트 제거
            attemptCountMap.remove(order.orderId)

        } catch (e: Exception) {
            logger.error("❌ 처리 실패 (시도: $attemptCount/3): ${e.message}")

            // 3번 이상 실패하면 카운트 제거 (더 이상 재시도 안함)
            if (attemptCount >= 3) {
                attemptCountMap.remove(order.orderId)
                logger.error("💀 최종 실패! 더 이상 재시도하지 않습니다.")
            }

            throw e  // 🔥 예외를 던져서 재시도 트리거
        }

        logger.info("✅ 주문 처리 완료!")
    }

    private fun processOrder(order: OrderMessage) {
        // 실제 비즈니스 로직
        logger.info("💰 주문 금액: ${order.quantity * order.price}원")
    }
}

// Consumer용 DTO
data class OrderMessage(
    val orderId: String,
    val productName: String,
    val quantity: Int,
    val price: Int
)
