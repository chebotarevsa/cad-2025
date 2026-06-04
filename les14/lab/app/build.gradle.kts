plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation(libs.spring.context)
    implementation(libs.spring.webmvc)
    implementation(libs.spring.orm)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.hikari)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.jsr310)
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")

    implementation(libs.spring.security.web)
    implementation(libs.spring.security.config)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.war {
    archiveFileName.set("ROOT.war")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
