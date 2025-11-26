plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":default-core"))
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // 스케줄링
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    // API 호출배치
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
}
