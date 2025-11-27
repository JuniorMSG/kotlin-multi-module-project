package com.ms.multi.infrastructure

// infrastructure/KakaoPayPayment.kt

import com.ms.multi.domain.Payment
import com.ms.multi.domain.PaymentResult
import com.ms.multi.domain.PaymentStatus
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * 카카오페이 결제 구현체 (Adapter)
 */
@Component
class KakaoPayPayment : Payment {

    override fun process(amount: Long): PaymentResult {
        // 실제로는 카카오페이 API 호출
        println("[KakaoPay] 결제 처리 중: ${amount}원")

        return PaymentResult(
            transactionId = "KAKAO-${UUID.randomUUID()}",
            amount = amount,
            status = PaymentStatus.SUCCESS,
            paymentMethod = getPaymentMethodName()
        )
    }

    override fun getPaymentMethodName(): String = "KAKAO_PAY"
}

