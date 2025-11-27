package com.ms.multi.application

import com.ms.multi.domain.PaymentResult
import com.ms.multi.domain.PaymentType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 결제 서비스
 */
@Service
class PaymentService(
    private val paymentFactory: PaymentFactory,
) {
    /**
     * 결제 처리
     */
    @Transactional
    fun processPayment(
        paymentType: String,
        amount: Long,
    ): PaymentResult {
        // 1. 팩토리로 적절한 결제 객체 생성
        val payment = paymentFactory.create(paymentType)

        // 2. 결제 처리
        val result = payment.process(amount)

        // 3. 결제 내역 저장 (실제로는 Repository 사용)
        println("[PaymentService] 결제 완료: ${result.transactionId}")

        return result
    }

    /**
     * 사용 가능한 결제 수단 목록
     */
    fun getAvailablePaymentMethods(): List<String> {
        return PaymentType.values().map { it.name }
    }
}
