plugins {
    kotlin("jvm") version "1.9.10"
    id("org.springframework.boot") version "2.7.5"
    id("java")
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
   // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation(libs.hikari)
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation(libs.hibernate.hikaricp)
    implementation(libs.hibernate.core)
    implementation(libs.slf4j.api)
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    runtimeOnly(libs.h2)

    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation(kotlin("script-runtime"))
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.11.1")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Define the main class for the application.
    mainClass = "ru.bsuedu.cad.lab.App"
}
