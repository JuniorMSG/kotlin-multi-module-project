plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    // Core 모듈 의존성
    implementation(project(":default-core"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // ✅ Kafka Producer 의존성
    implementation("org.springframework.kafka:spring-kafka")

    // JSON 직렬화
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // ===== 캐시 관련 의존성 추가 =====
    // 1. Spring Cache 추상화 (필수)
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // 2. Redis Cache
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.lettuce:lettuce-core") // Redis 클라이언트

    // 3. Caffeine Cache (로컬 고성능 캐시)
    implementation("com.github.ben-manes.caffeine:caffeine")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
