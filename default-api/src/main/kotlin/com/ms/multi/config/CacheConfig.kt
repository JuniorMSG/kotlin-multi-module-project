package com.ms.multi.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@ConfigurationProperties(prefix = "caffeine")
data class CacheProperties(
    val defaultExpireAfterWrite: Long = 60,
    val defaultMaximumSize: Long = 200,
    val defaultTimeUnit: TimeUnit = TimeUnit.SECONDS,
    val caches: List<CacheConfig> = emptyList(),
) {
    data class CacheConfig(
        val name: String,
        val expireAfterWrite: Long? = null,
        val maximumSize: Long? = null,
        val timeUnit: TimeUnit? = null,
    )
}

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties::class)
class CacheConfig {
    @Bean
    fun cacheManager(cacheProperties: CacheProperties): CacheManager {
        val cacheManager = CaffeineCacheManager()

        // 기본 캐시 설정
        cacheManager.setCaffeine(
            Caffeine
                .newBuilder()
                .expireAfterWrite(
                    cacheProperties.defaultExpireAfterWrite,
                    cacheProperties.defaultTimeUnit,
                )
                .maximumSize(cacheProperties.defaultMaximumSize),
        )

        cacheManager.isAllowNullValues = true

        cacheProperties.caches.forEach { cacheConfig ->
            val cache =
                Caffeine
                    .newBuilder()
                    .expireAfterWrite(
                        cacheConfig.expireAfterWrite ?: cacheProperties.defaultExpireAfterWrite,
                        cacheConfig.timeUnit ?: cacheProperties.defaultTimeUnit,
                    )
                    .maximumSize(cacheConfig.maximumSize ?: cacheProperties.defaultMaximumSize)
                    .build<Any, Any>()

            cacheManager.registerCustomCache(cacheConfig.name, cache)
        }

        return cacheManager
    }
}
