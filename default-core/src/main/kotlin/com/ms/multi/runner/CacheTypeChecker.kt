package com.ms.multi.runner

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@EnableCaching
@Component
class CacheTypeChecker(
    private val cacheManager: CacheManager
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        printCacheInfo()
    }

    private fun printCacheInfo() {
        logger.info("")
        logger.info("=" .repeat(70))
        logger.info("🔍 CACHE TYPE DETECTION")
        logger.info("=" .repeat(70))

        // CacheManager 타입 확인
        logger.info("📦 CacheManager Type: ${cacheManager.javaClass.name}")
        logger.info("📝 Cache Names: ${cacheManager.cacheNames}")

        // 실제 사용되는 캐시 타입 확인
        val cache = cacheManager.getCache("products")
        if (cache != null) {
            logger.info("🎯 Actual Cache Type: ${cache.javaClass.name}")
            logger.info("🔧 Native Cache Type: ${cache.nativeCache.javaClass.name}")
        }

        logger.info("")
        logger.info("=" .repeat(70))

        // 어떤 캐시가 선택되었는지 출력
        when (cacheManager) {
            is RedisCacheManager -> {
                logger.info("✅ SELECTED: REDIS CACHE")
                logger.info("   - Distributed cache")
                logger.info("   - Shared across multiple servers")
                logger.info("   - Persistent storage")
                logger.info("   - Network latency exists")
            }
            is CaffeineCacheManager -> {
                logger.info("✅ SELECTED: CAFFEINE CACHE")
                logger.info("   - Local in-memory cache")
                logger.info("   - High performance")
                logger.info("   - Advanced eviction policies")
                logger.info("   - Statistics support")
            }
            is ConcurrentMapCacheManager -> {
                logger.info("✅ SELECTED: SIMPLE CACHE (ConcurrentHashMap)")
                logger.info("   - Basic local cache")
                logger.info("   - No eviction policy")
                logger.info("   - No statistics")
                logger.info("   - Good for testing")
            }
            else -> {
                logger.error("❌ UNKNOWN CACHE TYPE: ${cacheManager.javaClass.name}")
            }
        }

        logger.info("=" .repeat(70))
        logger.info("")
    }
}
