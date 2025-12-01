plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

// 🎯 detekt 태스크 완전 비활성화
tasks.matching { it.name.startsWith("detekt") }.configureEach {
    enabled = false
}
