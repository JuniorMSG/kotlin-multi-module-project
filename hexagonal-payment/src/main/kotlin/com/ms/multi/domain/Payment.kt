package com.ms.multi.domain

/**
 * 결제 인터페이스 (Port)
 */
interface Payment {
    /**
     * 결제 처리
     */
    fun process(amount: Long): PaymentResult

    /**
     * 결제 수단 이름
     */
    fun getPaymentMethodName(): String
}
