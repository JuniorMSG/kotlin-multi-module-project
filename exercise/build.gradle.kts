plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
}

dependencies {
}

detekt {
    source.setFrom(files())
}

// 또는 태스크 비활성화
tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    enabled = false
}
