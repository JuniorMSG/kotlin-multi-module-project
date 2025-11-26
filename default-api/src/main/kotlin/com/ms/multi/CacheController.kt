package com.ms.multi

import com.ms.multi.service.CacheService
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/api/cache")
class CacheController(
    private val productService: CacheService,
    private val cacheManager: CacheManager
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/info")
    fun getCacheInfo(): Map<String, Any> {
        val cache = cacheManager.getCache("products")

        return mapOf(
            "cacheManagerType" to cacheManager.javaClass.simpleName,
            "cacheType" to (cache?.javaClass?.simpleName ?: "null"),
            "nativeCacheType" to (cache?.nativeCache?.javaClass?.simpleName ?: "null"),
            "cacheNames" to cacheManager.cacheNames
        )
    }

    @GetMapping("/test/{id}")
    fun testCache(@PathVariable
    id: String): Map<String, Any> {
        val times = mutableListOf<Long>()

        repeat(3) { i ->
            val time = measureTimeMillis {
                productService.getProduct(id)
            }
            times.add(time)
            logger.info("⏱️  Try ${i + 1}: ${time}ms")
        }

        return mapOf(
            "cacheType" to cacheManager.javaClass.simpleName,
            "times" to mapOf(
                "1st" to "${times[0]}ms (DB)",
                "2nd" to "${times[1]}ms (Cache)",
                "3rd" to "${times[2]}ms (Cache)"
            )
        )
    }

    @GetMapping("/test/delete")
    fun deleteCache() {
        productService.evictCache()
    }
}
