package com.ms.multi.domain

/**
 * 결제 타입
 */
enum class PaymentType {
    CREDIT_CARD,
    KAKAO_PAY,
    ;

    companion object {
        fun fromString(type: String): PaymentType {
            return try {
                valueOf(type.uppercase().replace("-", "_"))
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("지원하지 않는 결제 수단입니다: $type")
            }
        }
    }
}
