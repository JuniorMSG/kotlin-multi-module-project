package com.ms.multi.infrastructure

import com.ms.multi.domain.Payment
import com.ms.multi.domain.PaymentResult
import com.ms.multi.domain.PaymentStatus
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * 신용카드 결제 구현체 (Adapter)
 */
@Component
class CreditCardPayment : Payment {

    override fun process(amount: Long): PaymentResult {
        // 실제로는 PG사 API 호출
        println("[CreditCard] 결제 처리 중: ${amount}원")

        return PaymentResult(
            transactionId = "CC-${UUID.randomUUID()}",
            amount = amount,
            status = PaymentStatus.SUCCESS,
            paymentMethod = getPaymentMethodName()
        )
    }

    override fun getPaymentMethodName(): String = "CREDIT_CARD"
}
