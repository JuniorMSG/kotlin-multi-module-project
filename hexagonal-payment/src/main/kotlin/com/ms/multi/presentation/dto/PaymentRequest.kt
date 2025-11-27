package com.ms.multi.presentation.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

/**
 * 결제 요청 DTO
 */
data class PaymentRequest(
    @field:NotBlank(message = "결제 수단을 입력해주세요")
    val paymentType: String,

    @field:Min(value = 100, message = "최소 결제 금액은 100원입니다")
    val amount: Long
)
