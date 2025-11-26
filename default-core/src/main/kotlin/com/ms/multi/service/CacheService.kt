package com.ms.multi.service

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.Serializable

@Service
class CacheService {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["products"], key = "#id")
    fun getProduct(id: String): Product {
        logger.info("💾 [DB Query] Fetching: $id")
        Thread.sleep(1000)
        return Product(id, "Product $id")
    }

    @CacheEvict(cacheNames = ["products"], allEntries = true)
    fun evictCache() {
        println("evictCache")
    }


    data class Product(
        val id: String,
        val name: String
    ) : Serializable  // 👈 이것만 추가!
}
