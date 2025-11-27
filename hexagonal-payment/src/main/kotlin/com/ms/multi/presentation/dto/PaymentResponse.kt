package com.ms.multi.presentation.dto

import java.time.LocalDateTime

/**
 * 결제 응답 DTO
 */
data class PaymentResponse(
    val transactionId: String,
    val success: Boolean,
    val message: String,
    val paymentMethod: String,
    val amount: Long,
    val processedAt: LocalDateTime,
)
