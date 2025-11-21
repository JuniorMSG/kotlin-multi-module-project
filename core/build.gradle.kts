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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // mysql
    implementation("mysql:mysql-connector-java:$mysqlConnectorJavaVersion")

    // 쿼리 노출 모듈
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:$p6SpySpringBootStarterVersion")

    detektPlugins("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.21")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}
