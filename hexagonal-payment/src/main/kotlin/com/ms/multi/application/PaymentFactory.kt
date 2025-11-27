package com.ms.multi.application

import com.ms.multi.domain.Payment
import com.ms.multi.domain.PaymentType
import com.ms.multi.infrastructure.CreditCardPayment
import com.ms.multi.infrastructure.KakaoPayPayment
import org.springframework.stereotype.Component

/**
 * 결제 팩토리 (Simple Factory Pattern)
 */
@Component
class PaymentFactory(
    private val creditCardPayment: CreditCardPayment,
    private val kakaoPayPayment: KakaoPayPayment,
) {
    /**
     * 결제 타입에 따라 적절한 Payment 객체 생성
     */
    fun create(type: PaymentType): Payment {
        return when (type) {
            PaymentType.CREDIT_CARD -> creditCardPayment
            PaymentType.KAKAO_PAY -> kakaoPayPayment
        }
    }

    /**
     * 문자열로 결제 객체 생성
     */
    fun create(typeString: String): Payment {
        val type = PaymentType.fromString(typeString)
        return create(type)
    }
}
