package com.ms.multi.domain.product.dto

import java.io.Serializable

data class ProductResult(
    val id: Long? = null,
    val name: String,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
