plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation(libs.spring.web)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.hikari)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.jackson.databind)
    runtimeOnly(libs.h2)
    providedCompile(libs.jakarta.servlet.api)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("pet-shop.war")
}
