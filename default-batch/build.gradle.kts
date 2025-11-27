plugins {
    id("org.springframework.boot")
}

val mysqlConnectorJavaVersion = "8.0.33"
dependencies {
    implementation(project(":default-core"))
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // 스케줄링
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    // API 호출배치
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("mysql:mysql-connector-java:${mysqlConnectorJavaVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
}
