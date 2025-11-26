package com.ms.multi.service

import com.ms.multi.domain.product.dto.ProductResult
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Suppress("MagicNumber")
@Service
class CacheService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["products"], key = "#id")
    fun getProduct(id: Long): ProductResult {
        logger.info("💾 [DB Query] Fetching: $id")
        Thread.sleep(1000)
        return ProductResult(id, "ProductResult $id")
    }

    @CacheEvict(cacheNames = ["products"], allEntries = true)
    fun evictCache() {
        println("evictCache")
    }
}
