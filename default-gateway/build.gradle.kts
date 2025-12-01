plugins {
    id("org.springframework.boot")
}
val mysqlConnectorJavaVersion = "8.0.33"
dependencies {
    implementation(project(":default-core"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Cache 추상화 (필수)
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")
    implementation("io.lettuce:lettuce-core")

    runtimeOnly("mysql:mysql-connector-java:$mysqlConnectorJavaVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
