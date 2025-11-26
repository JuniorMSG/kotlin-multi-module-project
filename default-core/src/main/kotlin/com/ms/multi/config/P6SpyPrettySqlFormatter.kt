package com.ms.multi.config

import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import kotlin.collections.any
import kotlin.text.isNullOrBlank
import kotlin.text.startsWith
import kotlin.text.trimStart

class P6SpyPrettySqlFormatter : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String?,
        prepared: String?,
        sql: String?,
        url: String?,
    ): String {
        val result =
            when {
                sql.isNullOrBlank() -> ""
                category == CATEGORY_BATCH && elapsed == 0L -> ""
                category != CATEGORY_STATEMENT && category != CATEGORY_BATCH -> ""
                else -> {
                    val trimmedSql = sql.trimStart()
                    val formatter =
                        if (DDL_KEYWORDS.any { trimmedSql.startsWith(it, ignoreCase = true) }) {
                            FormatStyle.DDL.formatter
                        } else {
                            FormatStyle.BASIC.formatter
                        }
                    "Execution Time: ${elapsed}ms | Category: $category\n${formatter.format(sql)}"
                }
            }

        return result
    }

    private companion object {
        private const val CATEGORY_STATEMENT = "statement"
        private const val CATEGORY_BATCH = "batch"
        private val DDL_KEYWORDS = listOf("create", "alter", "drop")
    }
}
