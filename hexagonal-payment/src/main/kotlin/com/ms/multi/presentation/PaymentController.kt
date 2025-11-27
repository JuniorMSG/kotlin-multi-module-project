package com.ms.multi.presentation

import com.ms.multi.application.PaymentService
import com.ms.multi.domain.PaymentStatus
import com.ms.multi.presentation.dto.PaymentRequest
import com.ms.multi.presentation.dto.PaymentResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 결제 컨트롤러
 */
@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    /**
     * 결제 처리
     */
    @PostMapping
    fun processPayment(
        @Valid
        @RequestBody
        request: PaymentRequest
    ): ResponseEntity<PaymentResponse> {
        val result = paymentService.processPayment(
            paymentType = request.paymentType,
            amount = request.amount
        )

        val response = PaymentResponse(
            transactionId = result.transactionId,
            success = result.status == PaymentStatus.SUCCESS,
            message = "${result.paymentMethod}로 ${result.amount}원이 결제되었습니다.",
            paymentMethod = result.paymentMethod,
            amount = result.amount,
            processedAt = result.processedAt
        )

        return ResponseEntity.ok(response)
    }

    /**
     * 사용 가능한 결제 수단 조회
     */
    @GetMapping("/methods")
    fun getPaymentMethods(): ResponseEntity<List<String>> {
        val methods = paymentService.getAvailablePaymentMethods()
        return ResponseEntity.ok(methods)
    }
}
