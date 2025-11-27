import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("plugin.jpa") version "2.0.21"
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
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

    // Spring Cache 추상화 (필수)
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")
    implementation("io.lettuce:lettuce-core")

    // mysql
    runtimeOnly("mysql:mysql-connector-java:$mysqlConnectorJavaVersion")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    testImplementation("org.springframework.security:spring-security-test")
    detektPlugins("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.21")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.set(
            listOf(
                "-Xjsr305=strict",
                "-java-parameters",
            ),
        )
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
