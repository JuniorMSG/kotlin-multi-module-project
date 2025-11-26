import org.gradle.kotlin.dsl.implementation

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("plugin.jpa") version "2.0.21"
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

val mysqlConnectorJavaVersion = "8.0.33"
val p6SpySpringBootStarterVersion = "1.11.0"
val jjwtVersion = "0.12.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // 쿼리 노출 모듈
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:$p6SpySpringBootStarterVersion")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT 의존성 추가
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")

    // mysql
    runtimeOnly("mysql:mysql-connector-java:$mysqlConnectorJavaVersion")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    testImplementation("org.springframework.security:spring-security-test")
    detektPlugins("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.21")
}

dependencies {
    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Redis Cache
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Caffeine Cache (Local In-Memory)
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Optional: Redis 연결 풀 최적화
    implementation("org.apache.commons:commons-pool2")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}
