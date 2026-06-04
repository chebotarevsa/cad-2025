plugins {
    java
    war
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation(libs.spring.webmvc)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.hikari)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.jackson.databind)
    implementation(libs.thymeleaf)
    implementation(libs.thymeleaf.spring6)
    runtimeOnly(libs.h2)
    providedCompile(libs.jakarta.servlet.api)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.spring.test)
    testImplementation(libs.assertj.core)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("pet-shop.war")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}
