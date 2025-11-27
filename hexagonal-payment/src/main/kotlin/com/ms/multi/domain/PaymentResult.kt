package com.ms.multi.domain

import java.time.LocalDateTime

/**
 * 결제 결과
 */
data class PaymentResult(
    val transactionId: String,
    val amount: Long,
    val status: PaymentStatus,
    val paymentMethod: String,
    val processedAt: LocalDateTime = LocalDateTime.now(),
)

enum class PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING,
}
