package com.ms.multi.common.dto
import com.ms.multi.exception.CommonExceptionCode
import org.springframework.data.domain.Page

typealias StandardListResponseDto<T> = StandardResponseDto<StandardResponseDto.Items<T>>
typealias StandardPagedResponseDto<T> = StandardResponseDto<StandardResponseDto.PageInfoItems<T>>

data class StandardResponseDto<T>(
    val meta: Meta,
    val data: T?,
) {
    data class Meta(
        val result: Result,
        val errorCode: String?,
        val message: String?,
    )

    data class Items<T>(
        val items: List<T>,
    )

    data class PaginationInfo(
        val page: Int,
        val size: Int,
        val totalCount: Long,
        val totalPages: Int,
    )

    data class PageInfoItems<T>(
        val items: List<T>,
        val pageInfo: PaginationInfo,
    )

    enum class Result {
        SUCCESS,
        FAIL,
    }

    companion object {
        fun <T> success(data: T? = null): StandardResponseDto<T> =
            StandardResponseDto(
                meta =
                    Meta(
                        result = Result.SUCCESS,
                        errorCode = null,
                        message = null,
                    ),
                data = data,
            )

        @JvmName("successList")
        fun <T> success(data: List<T>): StandardResponseDto<Items<T>> = success(Items(data))

        fun <T> success(page: Page<T>): StandardResponseDto<PageInfoItems<T>> =
            StandardResponseDto(
                meta =
                    Meta(
                        result = Result.SUCCESS,
                        errorCode = null,
                        message = null,
                    ),
                data =
                    PageInfoItems(
                        items = page.content,
                        pageInfo =
                            PaginationInfo(
                                page = page.number,
                                size = page.size,
                                totalCount = page.totalElements,
                                totalPages = page.totalPages,
                            ),
                    ),
            )

        fun <T> fail(
            errorCode: String,
            message: String,
            data: T? = null,
        ): StandardResponseDto<T> =
            StandardResponseDto(
                meta =
                    Meta(
                        result = Result.FAIL,
                        errorCode = errorCode,
                        message = message,
                    ),
                data = data,
            )

        fun <T> fail(
            error: CommonExceptionCode,
            message: String? = null,
            data: T? = null,
        ): StandardResponseDto<T> =
            fail(
                errorCode = error.code,
                message = message ?: error.message,
                data = data,
            )
    }
}
