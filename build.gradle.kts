plugins {
    kotlin("jvm") version "2.0.21" apply false
    kotlin("plugin.spring") version "2.0.21" apply false
    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
    java
}

allprojects {
    group = "com.module"
    version = "0.0.1-SNAPSHOT"
	
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
	
    afterEvaluate {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            config.setFrom(files("$rootDir/detekt.yml"))
            buildUponDefaultConfig = true
            parallel = true
        }
    }
	
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
	
    val kotestVersion = "5.9.0"
    val kotestSpringVersion = "1.3.0"
	
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
        testImplementation("io.kotest:kotest-assertions-core")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
	
    tasks.withType<Test> {
        useJUnitPlatform()
    }
	
    tasks.named("build") {
        dependsOn("detekt")
    }
	
    tasks.named("clean") {
        finalizedBy("ktlintFormat")
    }
	
    tasks.named("ktlintFormat") {
        finalizedBy("detekt")
    }
	
    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }
	
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("2.0.21")
            }
        }
    }
}
