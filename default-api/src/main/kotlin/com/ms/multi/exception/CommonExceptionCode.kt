package com.ms.multi.exception

enum class CommonExceptionCode(
    val code: String,
    val message: String,
) {
    INVALID_PARAMETER("INVALID_PARAMETER", "요청 파라미터가 잘못되었습니다."),
}
