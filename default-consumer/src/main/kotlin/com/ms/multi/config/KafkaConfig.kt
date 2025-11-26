package com.ms.multi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ms.multi.consumer.OrderMessage
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.util.backoff.FixedBackOff
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
@EnableKafka
class KafkaConfig(
    private val kafkaProperties: KafkaProperties,
    private val validator: LocalValidatorFactoryBean,
    private val objectMapper: ObjectMapper,
) : KafkaListenerConfigurer {

    companion object {
        private const val TRUSTED_PACKAGES = "com.ms.multi.*"
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, OrderMessage>
    ): ConcurrentKafkaListenerContainerFactory<String, OrderMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderMessage>()
        factory.consumerFactory = consumerFactory

        // 🔥 재시도 설정 - 이게 핵심!
        val backOff = FixedBackOff(2000L, 3L)  // 2초마다, 3번 재시도
        val errorHandler = DefaultErrorHandler(backOff)

        factory.setCommonErrorHandler(errorHandler)

        return factory
    }

    // 🔥 핵심 1: ConsumerFactory 생성
    @Bean
    fun orderConsumerFactory(): ConsumerFactory<String, OrderMessage> {
        val deserializer = JsonDeserializer(OrderMessage::class.java, objectMapper).apply {
            addTrustedPackages(TRUSTED_PACKAGES)
            setUseTypeHeaders(false)
        }

        return DefaultKafkaConsumerFactory(
            kafkaProperties.buildConsumerProperties(),
            StringDeserializer(),
            ErrorHandlingDeserializer(deserializer)
        )
    }

    // 🔥 핵심 2: KafkaListenerContainerFactory 생성
    @Bean
    fun orderKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderMessage> {
        return ConcurrentKafkaListenerContainerFactory<String, OrderMessage>().apply {
            consumerFactory = orderConsumerFactory()
        }
    }

    override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
        registrar.setValidator(validator)
    }
}
