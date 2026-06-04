plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation(libs.spring.context)
    implementation(libs.spring.web)
    implementation(libs.spring.orm)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.hikari)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
